package leti.sidis.subs.command.message_broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Config {

    //TODO: routing keys e topic devem passar a app.properties ou .env

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("subscriptions.fanout");
    }

    @Bean
    public TopicExchange plansTopic(){return new TopicExchange("plan.topic");}

    @Bean
    public Queue subscriptionsQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue createPlanQueue(){return new AnonymousQueue();}

    @Bean
    public Queue updatePlanQueue(){return new AnonymousQueue();}


    @Bean
    public Binding subscriptionsBinding() {
        return BindingBuilder
                .bind(subscriptionsQueue())
                .to(fanout());
    }

    @Bean
    public Binding planCreatedBinding(){return BindingBuilder.bind(createPlanQueue()).to(plansTopic()).with("plan.created.*");}

    @Bean
    public Binding planUpdatedBinding(){return BindingBuilder.bind(updatePlanQueue()).to(plansTopic()).with("plan.updated.*");}

    @Bean
    public MessageConverter converter(){
        ObjectMapper dateMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(dateMapper);
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}