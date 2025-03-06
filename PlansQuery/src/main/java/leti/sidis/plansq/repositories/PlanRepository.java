package leti.sidis.plansq.repositories;

import leti.sidis.plansq.model.Plan;


public interface PlanRepository {

        Iterable<Plan> findAll();
        Plan findByPlanName(String planName);
        //boolean existsByPlanName(String planName);

}
