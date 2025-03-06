package leti.sidis.subs.query.services;


import leti.sidis.subs.query.model.Plan;
import leti.sidis.subs.query.model.Subscription;
import leti.sidis.subs.query.repositories.PlanRepository;
import leti.sidis.subs.query.repositories.SubscriptionRepositoryJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionRepositoryJPA subscriptionRepository;
    private final PlanRepository planDetailsRepository;


    public Iterable<Subscription> findAll() {
        Stream<Subscription> localSubscriptions = StreamSupport.stream(subscriptionRepository.findAll().spliterator(), false);

        return localSubscriptions.collect(Collectors.toList());
    }

    public Optional<Subscription> findOne(final UUID subscriptionId) {
        return Optional.ofNullable(subscriptionRepository.findBySubscriptionId(subscriptionId));
    }

    @Override
    public Plan getAssociatedPlanDetails(UUID subscriptionId) {

        Optional<Subscription> subscription = findOne(subscriptionId);

        if(subscription.isEmpty()) {
            throw new IllegalArgumentException("Couldn't find your subscription.");
        }

        Optional<Plan> planDetails = planDetailsRepository.findById(subscription.get().getPlanName());

        if(planDetails.isEmpty()) {
            throw new IllegalArgumentException("Couldn't find the associated plan.");
        }

        return planDetails.get();
    }

}