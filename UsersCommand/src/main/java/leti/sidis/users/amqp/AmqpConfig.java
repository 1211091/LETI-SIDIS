package leti.sidis.users.amqp;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {
    @Value("${user.topic.key}")
    String topic;

    @Value("${user.registered.key}")
    String createdKey;

    @Bean
    public Queue createUserQueue(){return new AnonymousQueue();}


    @Bean
    public TopicExchange topic(){return new TopicExchange(topic);}

    @Bean
    public Binding bindingCreate(){return BindingBuilder.bind(createUserQueue()).to(topic()).with(createdKey);}

    @Bean
    public MessageConverter converter(){
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return  new Jackson2JsonMessageConverter(objectMapper);}


    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
