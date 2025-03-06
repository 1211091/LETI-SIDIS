package leti.sidis.plans.repositories;

import leti.sidis.plans.model.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepositoryJPA extends CrudRepository<Plan, String>, PlanRepository {

}
