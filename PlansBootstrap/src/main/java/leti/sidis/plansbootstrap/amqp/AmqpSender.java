package leti.sidis.plansbootstrap.amqp;


import leti.sidis.plansbootstrap.model.Plan;
import leti.sidis.plansbootstrap.repositories.PlanRepositoryJPA;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmqpSender {
    @Value("${plan.topic.key}")
    String topic;

    @Value("${plan.created.key}")
    String createdKey;

    @Value("${plan.updated.key}")
    String updatedKey;

    @Value("${plan.bootstrapping.key}")
    String planBootstrappingKey;

    @Value("${plan.bootstrapping.queue}")
    String planBootstrappingQueue;

    @Value("${plan.bootstrapping.exchange}")
    String planBootstrappingDirectExchange;

    private RabbitTemplate rabbitTemplate;

    private PlanRepositoryJPA planRepository;

    public AmqpSender(RabbitTemplate rabbitTemplate, PlanRepositoryJPA planRepository){
        this.rabbitTemplate = rabbitTemplate;
        this.planRepository = planRepository;
    }

    //TODO: usar DTO
    public void sendPlanCreated(Plan plan){
        rabbitTemplate.convertAndSend(topic,createdKey,plan);
        System.out.println("[x] Sent Plan " + plan.getPlanName());
    }

    public void sendPlanUpdated(Plan plan){
        rabbitTemplate.convertAndSend(topic,updatedKey,plan);
        System.out.println("[x] Sent Plan " + plan.getPlanName() + " updated");
    }

    public void bootstrap(){
        List<Plan> planList = (List<Plan>) rabbitTemplate.convertSendAndReceive(planBootstrappingQueue, planBootstrappingKey, "plan bootstrap");
        assert planList != null;
        planRepository.saveAll(planList);
    }

}
