package leti.sidis.subs.command.services;

import leti.sidis.subs.command.api.SubscriptionDTO;
import leti.sidis.subs.command.model.BonusPlanRequest;
import leti.sidis.subs.command.model.Plan;
import leti.sidis.subs.command.model.Subscription;
import leti.sidis.subs.command.repositories.PlanRepository;
import leti.sidis.subs.command.repositories.SubscriptionRepositoryJPA;
import leti.sidis.subs.command.services.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private final SubscriptionRepositoryJPA repository;
    private final PlanRepository planRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionService subscriptionService;

    @RabbitListener(queues = "#{subscriptionsQueue.name}")
    public void notify(SubscriptionDTO subscriptionDTO, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "subscription.created", "subscription.renewed", "subscription.cancelled", "plan.changed":
                logger.info("Received subscription with id: {}", subscriptionDTO.getSubscriptionId());
                if(repository.existsById(subscriptionDTO.getSubscriptionId())) {
                    repository.deleteById(subscriptionDTO.getSubscriptionId());
                }
                Subscription subscription = subscriptionMapper.toSubscription(subscriptionDTO);
                repository.save(subscription);
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }

    @RabbitListener(queues = {"#{createPlanQueue.name}", "#{updatePlanQueue.name}"})
    public void receivePlan(Plan plan, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key) {
        logger.info("Received plan {} with key {}", plan.getPlanName(), key);
        if (planRepository.existsById(plan.getPlanName())) {
            planRepository.deleteById(plan.getPlanName());
        }
        planRepository.save(plan);
    }

    @RabbitListener(queues = "bonusplan.requests")
    public boolean receiverCreatedBonusPlan(BonusPlanRequest request){
        return subscriptionService.createBonusSub(request);
    }

}
