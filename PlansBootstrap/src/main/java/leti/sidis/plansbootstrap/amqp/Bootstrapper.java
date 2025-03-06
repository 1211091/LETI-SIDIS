package leti.sidis.plansbootstrap.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Bootstrapper implements CommandLineRunner {
    private final AmqpSender sender;
    private final RabbitListenerEndpointRegistry registry;

    @Override
    public void run(String... args) {
        sender.bootstrap();
        registry.start();
    }
}
