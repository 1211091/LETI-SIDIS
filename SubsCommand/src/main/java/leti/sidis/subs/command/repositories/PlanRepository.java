package leti.sidis.subs.command.repositories;

import leti.sidis.subs.command.model.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends CrudRepository<Plan, String> {}