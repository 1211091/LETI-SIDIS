package leti.sidis.plansq.services;


import leti.sidis.plansq.api.CreatePlanRequest;
import leti.sidis.plansq.api.EditPlanRequest;
import leti.sidis.plansq.model.Plan;

import java.util.Optional;

public interface PlanService {

  //  Plan createPlan(CreatePlanRequest resource);
   Iterable<Plan> findAll();
   // Iterable<Plan> findAllExternal();
    Optional<Plan> findOne(String planName);
  //  Optional<Plan> findOneExternal(String planName);
  //  Plan update(String planName, EditPlanRequest resource);
  //  Plan updateExternalPlan(String planName, EditPlanRequest resource);
  //  Plan cease(String planName);
 //   Plan ceaseExternal(String planName);

    Plan updatePlanAmqp(Plan plan);
/* // Plan cancel(String planName);
    Plan updatePrice(String planName, EditPriceRequest resource,String userName);
    Plan promote(String planName);
    Iterable<PriceHistory> getPriceHistory(String planName);*/
}
