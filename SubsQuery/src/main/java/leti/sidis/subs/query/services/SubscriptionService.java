package leti.sidis.subs.query.services;

import leti.sidis.subs.query.api.EditSubscriptionRequest;
import leti.sidis.subs.query.model.Plan;
import leti.sidis.subs.query.model.PlanDetails;
import leti.sidis.subs.query.model.Subscription;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public interface SubscriptionService {

    Iterable<Subscription> findAll();
    /**
     * Finds a subscription by id.
     *
     * @param subscriptionId
     * @return
     */
    Optional<Subscription> findOne(UUID subscriptionId);

    Plan getAssociatedPlanDetails(UUID subscriptionId);




    //List<Integer> findByMonth(String month, Subscription subscription);



    /*Subscription switchPlan(SwitchPlanRequest resource, Long userId);

    /**
     * Renews annual subscription
     * @param Id
     * @return
     */




    //Plan getPlanDetails(Long id);
    //String getJoke(Long Id) throws IOException;

    //String getWeather(Long Id, WeatherForecastRequest resource) throws IOException;

}
