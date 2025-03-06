package leti.sidis.plansbootstrap.repositories;

import leti.sidis.plansbootstrap.model.Plan;


public interface PlanRepository {

        Iterable<Plan> findAll();
        Plan findByPlanName(String planName);
        //boolean existsByPlanName(String planName);

}
