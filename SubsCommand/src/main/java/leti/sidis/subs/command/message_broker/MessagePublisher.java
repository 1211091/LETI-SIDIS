package leti.sidis.subs.command.message_broker;

import leti.sidis.subs.command.api.SubscriptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisher {

    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private FanoutExchange fanout;

    public void publishSubscriptionCreated(SubscriptionDTO subscription) {
        template.convertAndSend(fanout.getName(), "subscription.created", subscription);
        logger.info("Sent subscription.created --> ");
    }

    public void publishSubscriptionRenewd(SubscriptionDTO subscription) {
        template.convertAndSend(fanout.getName(), "subscription.renewed", subscription);
        logger.info("Sent subscription.renewed --> ");
    }

    public void publishSubscriptionCancelled(SubscriptionDTO subscription) {
        template.convertAndSend(fanout.getName(), "subscription.cancelled", subscription);
        logger.info("Sent subscription.cancelled --> ");
    }

    public void publishPlanChanged(SubscriptionDTO subscription) {
        template.convertAndSend(fanout.getName(), "plan.changed", subscription);
        logger.info("Sent plan.changed --> ");
    }
}
