package leti.sidis.plans.services;


import leti.sidis.plans.api.CreatePlanRequest;
import leti.sidis.plans.api.EditPlanRequest;
import leti.sidis.plans.model.Plan;

import java.util.Optional;
import java.util.UUID;

public interface PlanService {

    Plan createPlan(CreatePlanRequest resource);
    Plan createPlanBonus (CreatePlanRequest resource, UUID userId);
  // Iterable<Plan> findAll();
  //  Iterable<Plan> findAllExternal();
    Optional<Plan> findOne(String planName);
  //  Optional<Plan> findOneExternal(String planName);
    Plan update(String planName, EditPlanRequest resource);
  //  Plan updateExternalPlan(String planName, EditPlanRequest resource);
    Plan cease(String planName);
 //  Plan ceaseExternal(String planName);

    Plan updatePlanAmqp(Plan plan);
/* // Plan cancel(String planName);
    Plan updatePrice(String planName, EditPriceRequest resource,String userName);
    Plan promote(String planName);
    Iterable<PriceHistory> getPriceHistory(String planName);*/
}
