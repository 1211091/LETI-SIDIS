/*
package leti.sidis.subs.query.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.cdimascio.dotenv.Dotenv;
import leti.sidis.subs.query.model.PlanDetails;
import leti.sidis.subs.query.model.Subscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Repository
public class SubscriptionRepositoryHTTP implements SubscriptionRepository{
    @Value("${server.port}")
    int currentPort;
    private final HttpClient client = HttpClient.newBuilder().build();
    private final Dotenv dotenv = Dotenv.load();
    private final int plansApiPort1 = Integer.parseInt(dotenv.get("PLANS_API_PORT1"));
    private final int plansApiPort2 = Integer.parseInt(dotenv.get("PLANS_API_PORT2"));
    private final int subscriptionsApiPort1 = Integer.parseInt(dotenv.get("SUBSCRIPTIONS_API_PORT1"));
    private final int subscriptionsApiPort2 = Integer.parseInt(dotenv.get("SUBSCRIPTIONS_API_PORT2"));

    @Override
    public Iterable<Subscription> findAll() {
        int targetPort = (currentPort == subscriptionsApiPort1) ? subscriptionsApiPort2 : subscriptionsApiPort1;
        List<Subscription> subscriptions = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:" + targetPort + "/api/subscriptions/external"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


                // Configure the ObjectMapper to use the JSR310 module for date/time handling
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                // Deserialize the JSON response into a list of Subscription objects
                subscriptions = objectMapper.readValue(response.body(), new TypeReference<List<Subscription>>() {});
            }


        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return subscriptions;
    }

    @Override
    public Subscription findBySubscriptionId(UUID subscriptionId) {
        int targetPort = (currentPort == subscriptionsApiPort1) ? subscriptionsApiPort2 : subscriptionsApiPort1;


        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:" + targetPort + "/api/subscriptions/" + subscriptionId + "/external"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                Subscription subscription = objectMapper.readValue(response.body(), Subscription.class);

                return subscription;
            } else if (response.statusCode() == 404) {
                return null; // Subscription not found
            } else {
                throw new RuntimeException("HTTP error: " + response.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException("Error while making the HTTP request", ex);
        }
    }

    public PlanDetails fetchAssociatedPlanDetails(String planName) {
        int targetPort = (currentPort == plansApiPort1) ? plansApiPort2 : plansApiPort1;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:" + targetPort + "/api/plans/" + planName))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                return objectMapper.readValue(response.body(), PlanDetails.class);

            } else if (response.statusCode() == 404) {
                throw new RuntimeException("No plan was found.");
            } else {
                throw new RuntimeException("HTTP error: " + response.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException("Error while making the HTTP request", ex);
        }
    }

*/
/*    public boolean checkIfPlanIsActive(String planName) {
        int targetPort = (currentPort == plansApiPort1) ? plansApiPort2 : plansApiPort1;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:" + targetPort + "/api/plans/" + planName))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                JsonNode jsonNode = objectMapper.readTree(response.body());
                return jsonNode.get("active").asBoolean();
            } else if (response.statusCode() == 404) {
                return false;
            } else {
                throw new RuntimeException("HTTP error: " + response.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException("Error while making the HTTP request", ex);
        }
    }*//*



    */
/*
    public Optional<Object> getPlanDetails(String planName) {
        int targetPort = (currentPort == plansApiPort1) ? plansApiPort2 : plansApiPort1;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:" + targetPort + "/api/plans/" + planName))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.body());
                return Optional.of(jsonNode);
            } else if (response.statusCode() == 404) {
                return Optional.empty();
            } else {
                throw new RuntimeException("HTTP error: " + response.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException("Error while making the HTTP request", ex);
        }
    }*//*


    public Subscription cancel(UUID subscriptionId) {
        int targetPort = (currentPort == subscriptionsApiPort1) ? subscriptionsApiPort2 : subscriptionsApiPort1;


        try {
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .registerModule(new JavaTimeModule());

            Optional<Subscription> subscription = Optional.ofNullable(findBySubscriptionId(subscriptionId));

            if(subscription.isPresent()) {
                subscription.get().setActive(false);
            }

            String json = objectMapper.writeValueAsString(subscription);


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:" + targetPort + "/api/subscriptions/" + subscriptionId + "/external"))
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Subscription.class);
            } else {
                throw new RuntimeException("HTTP error: " + response.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException("Error while making the HTTP request", ex);
        }
    }
}
*/
