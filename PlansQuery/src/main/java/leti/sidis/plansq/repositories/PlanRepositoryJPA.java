package leti.sidis.plansq.repositories;

import leti.sidis.plansq.model.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepositoryJPA extends CrudRepository<Plan, String>, PlanRepository {

}
