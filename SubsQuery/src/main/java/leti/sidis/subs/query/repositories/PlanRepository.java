package leti.sidis.subs.query.repositories;

import leti.sidis.subs.query.model.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends CrudRepository<Plan, String> {}
