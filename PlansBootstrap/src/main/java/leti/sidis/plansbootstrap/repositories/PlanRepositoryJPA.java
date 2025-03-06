package leti.sidis.plansbootstrap.repositories;

import leti.sidis.plansbootstrap.model.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepositoryJPA extends CrudRepository<Plan, String>, PlanRepository {

}
