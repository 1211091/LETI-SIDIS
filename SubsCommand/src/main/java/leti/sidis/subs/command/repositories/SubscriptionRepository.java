package leti.sidis.subs.command.repositories;

import leti.sidis.subs.command.model.Subscription;

import java.util.UUID;


public interface SubscriptionRepository  {
    Subscription findBySubscriptionId(UUID id);
    Iterable<Subscription> findAll();
    //Subscription findByUserId(Long userId);
    //ArrayList<Subscription> findByPlanName(String planName);


}

