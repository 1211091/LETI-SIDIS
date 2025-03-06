/*
package leti.sidis.plansq.repositories;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.cdimascio.dotenv.Dotenv;
import leti.sidis.plansq.api.EditPlanRequest;
import leti.sidis.plansq.model.Plan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PlanRepositoryHTTP implements PlanRepository{

    @Value("${server.port}")
    int currentPort;
    private final HttpClient client = HttpClient.newBuilder().build();
    private final Dotenv dotenv = Dotenv.load();
    private final int plansCommandPort1 = Integer.parseInt(dotenv.get("PLANS_COMMAND_PORT1"));
    private final int plansCommandPort2 = Integer.parseInt(dotenv.get("PLANS_COMMAND_PORT2"));
    private final int plansQueriesPort1 = Integer.parseInt(dotenv.get("PLANS_QUERY_PORT1"));
    private final int plansQueriesPort2 = Integer.parseInt(dotenv.get("PLANS_QUERY_PORT2"));
    private final int subscriptionsApiPort1 = Integer.parseInt(dotenv.get("SUBSCRIPTIONS_API_PORT1"));
    private final int subscriptionsApiPort2 = Integer.parseInt(dotenv.get("SUBSCRIPTIONS_API_PORT2"));

    @Override
    public Iterable<Plan> findAll() {
        int targetPort = (currentPort == plansCommandPort1) ? plansCommandPort2 : plansCommandPort1;
        List<Plan> plans = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:" + targetPort + "/api/plans/external"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                                              .registerModule(new JavaTimeModule());

                // Deserialize the JSON response
                plans = objectMapper.readValue(response.body(), new TypeReference<List<Plan>>() {});
            }

        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return plans;
    }

    @Override
    public Plan findByPlanName(String planName) {
        int targetPort = (currentPort == plansCommandPort1) ? plansCommandPort2 : plansCommandPort1;


        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:" + targetPort + "/api/plans/" + planName + "/external"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                                              .registerModule(new JavaTimeModule());

                Plan plan = objectMapper.readValue(response.body(), Plan.class);

                return plan;

            } else if (response.statusCode() == 404) {
                return null;
            } else {
                throw new RuntimeException("HTTP error: " + response.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException("Error while making the HTTP request", ex);
        }
    }

    public Plan update(String planName, EditPlanRequest resource) {
        int targetPort = (currentPort == plansCommandPort1) ? plansCommandPort2 : plansCommandPort1;


        try {
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                                          .registerModule(new JavaTimeModule());

            Plan plan = findByPlanName(planName);

            if (plan != null) {
                plan.update(
                        resource.getNumberOfMinutes(),
                        resource.getPlanDescription(),
                        resource.getMaxUsers(),
                        resource.getMusicCollections(),
                        resource.getMusicSuggestions(),
                        resource.isActive(),
                        resource.isPromoted());

                String planJson = objectMapper.writeValueAsString(plan);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://localhost:" + targetPort + "/api/plans/" + planName + "/external"))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(planJson))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    return objectMapper.readValue(response.body(), Plan.class);
                } else {
                    throw new RuntimeException("HTTP error: " + response.statusCode());
                }
            } else {
                throw new IllegalArgumentException("The plan doesn't exist in the external source.");
            }
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException("Error while making the HTTP update request", ex);
        }
    }

    public List<Map<String, Object>> findSubscriptionsWithPlan(String planName) {
        int targetPort = (currentPort ==  subscriptionsApiPort1) ? subscriptionsApiPort2 : subscriptionsApiPort1;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:" + targetPort + "/api/subscriptions"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                ObjectMapper objectMapper = new ObjectMapper();

                return objectMapper.readValue(
                        response.body(),
                        new TypeReference<List<Map<String, Object>>>() {}
                );

            } else if (response.statusCode() == 404) {
            } else {
                throw new RuntimeException("HTTP error: " + response.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException("Error while making the HTTP request", ex);
        }
        return null;
    }


}
*/
