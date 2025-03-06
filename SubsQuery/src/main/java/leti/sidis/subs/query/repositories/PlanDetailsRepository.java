package leti.sidis.subs.query.repositories;

import leti.sidis.subs.query.model.PlanDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PlanDetailsRepository extends CrudRepository<PlanDetails, String> {
    PlanDetails findByPlanName(String planName);
}