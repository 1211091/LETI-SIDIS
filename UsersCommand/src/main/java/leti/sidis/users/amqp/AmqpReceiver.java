package leti.sidis.users.amqp;

import leti.sidis.users.model.User;

import leti.sidis.users.repositories.UserRepository;
import leti.sidis.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AmqpReceiver {
    private final UserService userService;
    private final UserRepository userRepository;

    @RabbitListener(queues = "#{createUserQueue.name}")
    public void receiveCreatedUser(User user) {
        if(!userService.usernameExists(user.getUsername())){
            System.out.println("Received User " + user.getUsername() + " created");
            userRepository.save(user);
        }
    }


}

