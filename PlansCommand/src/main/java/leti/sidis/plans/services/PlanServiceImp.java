package leti.sidis.plans.services;

import leti.sidis.plans.amqp.AmqpSender;
import leti.sidis.plans.api.CreatePlanRequest;
import leti.sidis.plans.api.EditPlanRequest;
import leti.sidis.plans.exceptions.ConflictException;
import leti.sidis.plans.exceptions.NotFoundException;
import leti.sidis.plans.model.Plan;
import leti.sidis.plans.repositories.PlanRepositoryHTTP;
import leti.sidis.plans.repositories.PlanRepositoryJPA;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class PlanServiceImp implements PlanService {

    private final PlanRepositoryJPA jpaRepository;
    private final EditPlanMapper planEditMapper;
    private final PlanRepositoryHTTP httpRepository;
    private final AmqpSender sender;

    public PlanServiceImp(PlanRepositoryJPA jpaRepository, EditPlanMapper planEditMapper, PlanRepositoryHTTP httpRepository, AmqpSender sender) {
        this.jpaRepository = jpaRepository;
        this.planEditMapper = planEditMapper;
        this.httpRepository = httpRepository;
        this.sender = sender;
    }

    @Override
    public Plan createPlan(final CreatePlanRequest resource) {

        if (jpaRepository.findByPlanName(resource.getPlanName()) != null) {
            throw new IllegalArgumentException("A plan with the name '" + resource.getPlanName() + "' already exists.");
        }

        final Plan plan = planEditMapper.create(resource);

        return jpaRepository.save(plan);
    }

    @Transactional
    @Override
    public Plan createPlanBonus(final CreatePlanRequest resource, UUID userId){
        if (jpaRepository.findByPlanName(resource.getPlanName()) != null) {
            throw new IllegalArgumentException("A plan with the name '" + resource.getPlanName() + "' already exists.");
        }else{
            final Plan plan = new Plan(
                    resource.getPlanName(),
                    resource.getNumberOfMinutes(),
                    resource.getPlanDescription(),
                    resource.getMusicCollections(),
                    resource.getMusicSuggestions(),
                    resource.getMonthlyFee(),
                    resource.getAnnualFee(),
                    true
            );
            Plan createdPlan = jpaRepository.save(plan);
            final var response = sender.sendAskCreatePlanBonus(userId.toString(),createdPlan.getPlanName());
            if (response){
                sender.sendPlanCreated(createdPlan);
                return createdPlan;
            }else{
                throw new RuntimeException("Error creating Plan Bonus");
            }
        }
    }

    public Iterable<Plan> findAll() {
        // instância atual
        Stream<Plan> localPlans = StreamSupport.stream(jpaRepository.findAll().spliterator(), false);

        return localPlans.collect(Collectors.toList());
    }
/*
    @Override
    public Iterable<Plan> findAllExternal() {
        return jpaRepository.findAll();
    }*/

    public Optional<Plan> findOne(final String planName) {

        return Optional.ofNullable(jpaRepository.findByPlanName(planName));
    }

/*
    @Override
    public Optional<Plan> findOneExternal(String planName) {
        return Optional.ofNullable(jpaRepository.findByPlanName(planName));
    }
*/

    @Override
    public Plan update(String planName, EditPlanRequest resource) {
        Plan plan = jpaRepository.findByPlanName(planName);

        if (plan != null) {
            Plan updatedPlan = plan.update(resource.getNumberOfMinutes(), resource.getPlanDescription(), resource.getMaxUsers(), resource.getMusicCollections(), resource.getMusicSuggestions(), resource.isActive(), resource.isPromoted());
            return jpaRepository.save(updatedPlan);
        } else { // o plano não existe na instância atual
                throw new IllegalArgumentException("This plan does not exist!");
            }
        }


  /*  public Plan updateExternalPlan(final String planName, final EditPlanRequest resource) {
        final var plan = jpaRepository.findByPlanName(planName);

        if (plan == null) {
            throw new IllegalArgumentException("This plan doesn't exist!");
        }
        plan.update(resource.getNumberOfMinutes(), resource.getPlanDescription(), resource.getMaxUsers(), resource.getMusicCollections(), resource.getMusicSuggestions(), resource.isActive(), resource.isPromoted());

        return jpaRepository.save(plan);
    }*/

    public Plan cease(String planName) {

       List<Map<String, Object>> subscriptions = httpRepository.findSubscriptionsWithPlan(planName);

        for (Map<String, Object> subscription : subscriptions) {
            if (subscription.get("planName").equals(planName)) {
                throw new IllegalArgumentException("Cannot cease a plan with subscriptions associated to it." +
                        " Please migrate the subscriptions to a different plan first.");
            }
        }

        Plan plan = jpaRepository.findByPlanName(planName);

        if (plan == null) {
            return null;
        } else if (plan.isActive()) {
            plan.setActive(false);
            return jpaRepository.save(plan);
        }

        throw new IllegalArgumentException("The plan has already been deactivated.");
    }

   /* public Plan ceaseExternal(String planName) {

        List<Map<String, Object>> subscriptions = httpRepository.findSubscriptionsWithPlan(planName);

        for (Map<String, Object> subscription : subscriptions) {
            if (subscription.get("planName").equals(planName)) {
                throw new IllegalArgumentException("Cannot cease a plan with subscriptions associated to it." +
                        " Please migrate the subscriptions to a different plan first.");
            }
        }

        Plan plan = httpRepository.findByPlanName(planName);

        if (plan != null) {
            throw new IllegalArgumentException("Couldn't find plan with name '" + planName + "'.");
        } else if (plan.isActive()) {
            plan.setActive(false);
            return jpaRepository.save(plan);
        }

        throw new IllegalArgumentException("The plan has already been deactivated.");
    }
*/
    @Override
    public Plan updatePlanAmqp(Plan plan) {
        Plan existingPlan = jpaRepository.findByPlanName(plan.getPlanName());

        if (existingPlan != null) {
            existingPlan.update(
                    plan.getNumberOfMinutes(),
                    plan.getPlanDescription(),
                    plan.getMaxUsers(),
                    plan.getMusicCollections(),
                    plan.getMusicSuggestions(),
                    plan.isActive(),
                    plan.isPromoted());
            return jpaRepository.save(existingPlan);

        }else{
            throw new NotFoundException("This plan does not exist!");
        }
    }


}


/*
    public Plan cancel(String planName) {
        final var localPlan = repository.findByPlanName(planName);

        if (localPlan != null) {
            if (!localPlan.isActive()) {
                throw new IllegalArgumentException("This plan is already deactivated!");
            }
            localPlan.setActive(false);
            return repository.save(localPlan);
        } else {
            // Plan does not exist in the local repository, check external repository
            Optional<Plan> externalPlan = planRepositoryHTTP.findByIdExternal(planName);

            if (externalPlan.isPresent()) {
                if (!externalPlan.get().isActive()) {
                    throw new IllegalArgumentException("This plan is already deactivated in the external repository!");
                } else {

                    externalPlan.get().setActive(false);
                    planRepositoryHTTP.updateExternalPlan(externalPlan.get().getPlanName());
                    return externalPlan.get();
                }
            } else {
                throw new IllegalArgumentException("This plan doesn't exist in both local and external repositories!");
            }
        }

    }*/

/*
    @Override
    public Plan updatePrice(String planName, EditPriceRequest resource, String userName) {
        final var plan = jpaRepository.findByPlanName(planName);
        PriceHistory priceHistory = new PriceHistory();
        double test = plan.getMonthlyFee();
        priceHistory.setPreviousMonthlyFee(plan.getMonthlyFee());
        priceHistory.setPreviousAnnualFee(plan.getAnnualFee());
        if (plan == null) {
            throw new IllegalArgumentException("This plan doesn't exist!");
        } else {

            priceHistory.setCurrentMonthlyFee(resource.getMonthlyFee());
            priceHistory.setCurrentAnnualFee(resource.getAnnualFee());
            priceHistory.setLastPriceChangeDate(LocalDate.now());
            priceHistory.setPlanName(plan.getPlanName());
            priceHistory.setUsername(userName);
            priceHistoryRepository.save(priceHistory);
            plan.applyPrice(resource.getMonthlyFee(), resource.getAnnualFee());

            return jpaRepository.save(plan);
        }
    }

    @Override
    public Plan promote(String planName) {
        final var plan = jpaRepository.findByPlanName(planName);
        if (plan == null) {
            throw new IllegalArgumentException("This plan doesn't exist!");
        } else {
            if (plan.isPromoted()) {
                throw new IllegalArgumentException("This plan is already promoted!");
            }
            plan.setPromoted(true);
            return jpaRepository.save(plan);
        }
    }

    @Override
    public Iterable<PriceHistory> getPriceHistory(String planName) {
        List<PriceHistory> pricesList;
        final var plan = jpaRepository.findByPlanName(planName);
        if (plan == null) {
            throw new IllegalArgumentException("This plan doesn't exist!");
        } else {
            pricesList = priceHistoryRepository.findByPlanNameOrderByLastPriceChangeDateDesc(planName);
            return pricesList;
        }
    }*/

