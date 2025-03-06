package leti.sidis.plansbootstrap.amqp;


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
    @Value("${plan.topic.key}")
    String topic;

    @Value("${plan.created.key}")
    String createdKey;

    @Value("${plan.updated.key}")
    String updatedKey;

    //Bootstrap
    @Value("${plan.bootstrapping.key}")
    String planBootstrappingKey;

    @Value("${plan.bootstrapping.queue}")
    String planBootstrappingQueue;

    @Value("${plan.bootstrapping.exchange}")
    String planBootstrappingDirectExchange;


    @Bean
    public Queue createPlanQueue(){return new AnonymousQueue();}

    @Bean
    public Queue updatePlanQueue(){return new AnonymousQueue();}

    @Bean
    public Queue bootstrapPlanQueue(){
        return new Queue(planBootstrappingQueue);
    }

    @Bean
    public TopicExchange topic(){return new TopicExchange(topic);}

    @Bean
    public DirectExchange directExchange(){return new DirectExchange(planBootstrappingDirectExchange);}

    @Bean
    public Binding bindingCreate(){return BindingBuilder.bind(createPlanQueue()).to(topic()).with(createdKey);}

    @Bean
    public Binding bindingUpdate(){return BindingBuilder.bind(updatePlanQueue()).to(topic()).with(updatedKey);}

    @Bean
    public Binding bindingBootstrapDevice(){
        return BindingBuilder.bind(bootstrapPlanQueue()).to(directExchange()).with(planBootstrappingKey);
    }
    @Bean
    public MessageConverter converter(){return new Jackson2JsonMessageConverter();}


    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }


}
