package leti.sidis.subs.command.services;

import leti.sidis.subs.command.api.EditSubscriptionRequest;
import leti.sidis.subs.command.api.SubscriptionDTO;
import leti.sidis.subs.command.model.BonusPlanRequest;
import leti.sidis.subs.command.model.Subscription;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public interface SubscriptionService {
    /**
     * Creates a new subscription.
     *
     * @param resource
     * @return
     */
    SubscriptionDTO create(EditSubscriptionRequest resource, UUID userId);

    /**
     * Finds a subscription by id.
     *
     * @param subscriptionId
     * @return
     */
    Optional<Subscription> findOne(UUID subscriptionId);

    /**
     * Finds all subscriptions.
     */
    Iterable<Subscription> findAll();

    /**
     * Renews a subscription.
     *
     * @param subscriptionId
     * @return
     */
    SubscriptionDTO renewSubscription(UUID subscriptionId);

    /**
     * Cancels a subscription.
     *
     * @param subscriptionId
     * @return
     */
    SubscriptionDTO cancel(UUID subscriptionId);

    /**
     * changes a subscription's plan.
     *
     * @param subscriptionId
     * @return Subscription
     */
    SubscriptionDTO changePlan(UUID subscriptionId, Map<String, String> requestBody);

    boolean createBonusSub(BonusPlanRequest request);
}
