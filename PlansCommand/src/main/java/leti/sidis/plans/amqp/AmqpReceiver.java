package leti.sidis.plans.amqp;

import leti.sidis.plans.exceptions.NotFoundException;
import leti.sidis.plans.repositories.PlanRepositoryJPA;
import leti.sidis.plans.services.PlanService;
import lombok.RequiredArgsConstructor;
import leti.sidis.plans.model.Plan;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO: Substituir sout
@Service
@RequiredArgsConstructor
public class AmqpReceiver {
    private final PlanRepositoryJPA planRepository;

    @RabbitListener(queues = "#{createPlanQueue.name}")
    public void receiveCreatedPlan(Plan plan) {
        if(planRepository.findByPlanName(plan.getPlanName()) == null){
            System.out.println("Received Plan " + plan.getPlanName() + " created");
            planRepository.save(plan);
        }
    }
    @RabbitListener(queues = "#{updatePlanQueue.name}")
    public void receiveUpdatedPlan(Plan plan) {
        if(planRepository.findByPlanName(plan.getPlanName()) != null){
            planRepository.deleteById(plan.getPlanName());
            System.out.println("Deleted Plan " + plan.getPlanName() + " outdated");
        }
        planRepository.save(plan);
        System.out.println("Received Plan " + plan.getPlanName() + " updated");
    }

    @RabbitListener(queues = "#{bootstrapPlanQueue.name")
    public List<Plan> sendAllPlans(){
        System.out.println("Sending Plans");
        return (List<Plan>) planRepository.findAll();
    }
}

