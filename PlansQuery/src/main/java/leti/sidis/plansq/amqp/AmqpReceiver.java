package leti.sidis.plansq.amqp;

import leti.sidis.plansq.exceptions.NotFoundException;
import leti.sidis.plansq.repositories.PlanRepositoryJPA;
import leti.sidis.plansq.services.PlanService;
import lombok.RequiredArgsConstructor;
import leti.sidis.plansq.model.Plan;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
