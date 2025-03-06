/*
 * Copyright (c) 2022-2022 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package leti.sidis.users.repositories;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import leti.sidis.users.api.UserView;
import leti.sidis.users.auth.AuthRequest;
import leti.sidis.users.model.Role;
import leti.sidis.users.model.User;
import leti.sidis.users.exceptions.NotFoundException;
import leti.sidis.users.api.Page;
import leti.sidis.users.api.SearchUsersQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Repository
@CacheConfig(cacheNames = "users")
public interface UserRepository extends UserRepoCustom, CrudRepository<User, Long> {

	@Override
	@CacheEvict(allEntries = true)
	<S extends User> List<S> saveAll(Iterable<S> entities);

	@Override
	@Caching(evict = { @CacheEvict(key = "#p0.id", condition = "#p0.id != null"),
			@CacheEvict(key = "#p0.username", condition = "#p0.username != null") })
	<S extends User> S save(S entity);

	/**
	 * findById searches a specific user and returns an optional
	 */
	@Cacheable
	Optional<User> findById(UUID objectId);

	/**
	 * getById explicitly loads a user or throws an exception if the user does not
	 * exist or the account is not enabled
	 *
	 * @param id
	 * @return
	 */
	@Cacheable
	default User getById(final UUID id) {
		final Optional<User> maybeUser = findById(id);
		// throws 404 Not Found if the user does not exist or is not enabled
		return maybeUser.filter(User::isEnabled).orElseThrow(() -> new NotFoundException(User.class, String.valueOf(id)));
	}

	@Cacheable
	Optional<User> findByUsername(String username);
}

/**
 * Custom interface to add custom methods to spring repository.
 *
 * @see //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.custom-implementations
 *
 *
 */
interface UserRepoCustom {

	List<User> searchUsers(Page page, SearchUsersQuery query);

	ResponseEntity<UserView> loginOnAnotherInstance(AuthRequest request);
}

/**
 * use JPA Criteria API to build the custom query
 *
 * @see //https://www.baeldung.com/hibernate-criteria-queries
 *
 */
@RequiredArgsConstructor
class UserRepoCustomImpl implements UserRepoCustom {

	// get the underlying JPA Entity Manager via spring thru constructor dependency
	// injection
	private final EntityManager em;

	@Value("${server.port}")
	int currentPort;
	private final HttpClient client = HttpClient.newBuilder().build();
	private final Dotenv dotenv = Dotenv.load();
	private final int usersApiPort1 = Integer.parseInt(dotenv.get("USERS_API_PORT1"));
	private final int usersApiPort2 = Integer.parseInt(dotenv.get("USERS_API_PORT2"));


	@Override
	public List<User> searchUsers(final Page page, final SearchUsersQuery query) {

		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<User> cq = cb.createQuery(User.class);
		final Root<User> root = cq.from(User.class);
		cq.select(root);

		final List<Predicate> where = new ArrayList<>();
		if (StringUtils.hasText(query.getUsername())) {
			where.add(cb.equal(root.get("username"), query.getUsername()));
		}
		if (StringUtils.hasText(query.getFullName())) {
			where.add(cb.like(root.get("fullName"), "%" + query.getFullName() + "%"));
		}

		// search using OR
		if (!where.isEmpty()) {
			cq.where(cb.or(where.toArray(new Predicate[0])));
		}

		cq.orderBy(cb.desc(root.get("createdAt")));

		final TypedQuery<User> q = em.createQuery(cq);
		q.setFirstResult((page.getNumber() - 1) * page.getLimit());
		q.setMaxResults(page.getLimit());

		return q.getResultList();
	}

	@Override
	public ResponseEntity<UserView> loginOnAnotherInstance(AuthRequest authRequest) {
		int targetPort = (currentPort == usersApiPort1) ? usersApiPort2 : usersApiPort1;

		String request = "{\"username\":\"" + authRequest.getUsername() + "\",\"password\":\"" + authRequest.getPassword() + "\"}";

		try {
			HttpRequest httpRequest =  HttpRequest.newBuilder()
					.uri(new URI("http://localhost:" + targetPort + "/api/public/internal/login"))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(request))
					.build();

			HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				ObjectMapper objectMapper = new ObjectMapper();

				String authToken = response.headers().firstValue("Authorization").orElse(null);

				JsonNode jsonResponse = objectMapper.readTree(response.body());

				UserView userView = new UserView();
				userView.setId((jsonResponse.get("id").asText()));
				userView.setUsername(jsonResponse.get("username").asText());
				userView.setFullName(jsonResponse.get("fullName").asText());

				/*JsonNode authorities = jsonResponse.get("authorities");
				Set<Role> roles = new HashSet<>();

				for (JsonNode authorityNode : authorities) {
					String authority = authorityNode.get("authority").asText();
					roles.add(new Role(authority));
				}*/

				return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, authToken).body(userView);
			}

		} catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        throw new NotFoundException("User couldn't be found");
    }
}
