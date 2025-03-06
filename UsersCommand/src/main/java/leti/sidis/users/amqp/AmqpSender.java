package leti.sidis.users.amqp;


import leti.sidis.users.api.UserView;
import leti.sidis.users.api.UserViewMapper;
import leti.sidis.users.model.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AmqpSender {
    @Value("${user.topic.key}")
    String topic;

    @Value("${user.registered.key}")
    String createdKey;


    private RabbitTemplate rabbitTemplate;


    public AmqpSender(RabbitTemplate rabbitTemplate){this.rabbitTemplate = rabbitTemplate;}

    public void sendUserCreated(User user){
    rabbitTemplate.convertAndSend(topic,createdKey, user);
    System.out.println("[x] Sent User " + user.getUsername());
    }


}
