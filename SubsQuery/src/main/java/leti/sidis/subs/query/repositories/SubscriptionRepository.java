package leti.sidis.subs.query.repositories;

import leti.sidis.subs.query.model.Subscription;

import java.util.UUID;


public interface SubscriptionRepository  {
    Subscription findBySubscriptionId(UUID id);
    Iterable<Subscription> findAll();
    //Subscription findByUserId(Long userId);
    //ArrayList<Subscription> findByPlanName(String planName);


}

