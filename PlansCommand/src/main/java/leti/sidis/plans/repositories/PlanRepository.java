package leti.sidis.plans.repositories;

import leti.sidis.plans.model.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;



public interface PlanRepository {

        Iterable<Plan> findAll();
        Plan findByPlanName(String planName);
        //boolean existsByPlanName(String planName);

}
