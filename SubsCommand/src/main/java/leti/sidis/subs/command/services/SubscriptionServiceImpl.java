package leti.sidis.subs.command.services;

import leti.sidis.subs.command.model.BonusPlanRequest;
import leti.sidis.subs.command.api.SubscriptionDTO;
import leti.sidis.subs.command.message_broker.MessagePublisher;
import leti.sidis.subs.command.model.Plan;
import leti.sidis.subs.command.repositories.PlanRepository;
import leti.sidis.subs.command.repositories.SubscriptionRepositoryJPA;
import leti.sidis.subs.command.api.EditSubscriptionRequest;
import leti.sidis.subs.command.model.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{

    private final EditSubscriptionMapper editSubscriptionMapper;
    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionRepositoryJPA subscriptionRepository;
    private final PlanRepository planRepository;
    private final MessagePublisher sender;

    @Override
    @Transactional
    public SubscriptionDTO create(EditSubscriptionRequest resource, UUID userId){

        // Averiguar se a o utilizador já tem alguma subscrição
        Iterable<Subscription> subscriptions = subscriptionRepository.findAll();
        for (Subscription subscription : subscriptions) {
            if (Objects.equals(subscription.getUserId(), userId)) {
                throw new IllegalArgumentException("You are already subscribed to a plan.");
            }
        }

        // se o plano existir: criar subscrição
        Optional<Plan> plan = planRepository.findById(resource.getPlanName());

        if(plan.isEmpty() || !plan.get().isActive()) {
            throw new IllegalArgumentException("The plan with name '" + resource.getPlanName() + "' either doesn't exist or is no longer active.");
        }

        Subscription subscription = editSubscriptionMapper.create(resource, userId);
        subscription.setSubscriptionId(UUID.randomUUID());
        subscription.startSubscription();

        subscriptionRepository.save(subscription);

        SubscriptionDTO subscriptionDTO = subscriptionMapper.toSubscriptionDTO(subscription);
        sender.publishSubscriptionCreated(subscriptionDTO);

        return subscriptionDTO;
    }

    @Override
    public SubscriptionDTO renewSubscription(UUID subscriptionId) {

        Optional<Subscription> subscription = findOne(subscriptionId);

        if (subscription.isPresent()) {
            String paymentFrequency = subscription.get().getPaymentFrequency();

            if (paymentFrequency.equals("Monthly")) {
                subscription.get().setLastRenovationDate(LocalDate.now());
                if (!subscription.get().isActive()) {subscription.get().setActive(true);}
                subscriptionRepository.save(subscription.get());

                SubscriptionDTO subscriptionDTO = subscriptionMapper.toSubscriptionDTO(subscription.get());
                sender.publishSubscriptionRenewd(subscriptionDTO);

                return subscriptionDTO;
            }
            LocalDate date = LocalDate.now();
            if (paymentFrequency.equals("Annually")
                    && date.getMonth()==subscription.get().getSubscriptionDate().getMonth()
                    && date.getDayOfMonth()==subscription.get().getSubscriptionDate().getDayOfMonth()
                    && (date.getYear()==subscription.get().getSubscriptionDate().getYear()+1))
            {
                subscription.get().setCancellationDate();
                throw new IllegalArgumentException("You can no longer renew your subscription! Please try to reactivate it first!");
            } else {
                subscription.get().setLastRenovationDate(LocalDate.now());
                subscriptionRepository.save(subscription.get());

                SubscriptionDTO subscriptionDTO = subscriptionMapper.toSubscriptionDTO(subscription.get());
                sender.publishSubscriptionRenewd(subscriptionDTO);

                return subscriptionDTO;
            }
        }
        throw new IllegalArgumentException("Couldn't find your subscription.");
    }

    @Override
    public SubscriptionDTO cancel(UUID subscriptionId) {
        Optional<Subscription> subscription = findOne(subscriptionId);

        if (subscription.isEmpty()) {
            throw new IllegalArgumentException("Couldn't find the subscription");
        }

        if (subscription.get().isActive()) {
            subscription.get().setCancellationDate();
            subscription.get().setActive(false);
            subscriptionRepository.save(subscription.get());

            SubscriptionDTO subscriptionDTO = subscriptionMapper.toSubscriptionDTO(subscription.get());
            sender.publishSubscriptionCancelled(subscriptionDTO);

            return subscriptionDTO;
        }
        throw new IllegalArgumentException("This subscription has already been deactivated.");
    }

    @Override
    /* Altera o plano a que uma subscrição está associada */
    public SubscriptionDTO changePlan(UUID subscriptionId, Map<String, String> requestBody) {

        // subscrição existe / está ativa
        Optional<Subscription> subscription = findOne(subscriptionId);

        if(subscription.isEmpty() || !subscription.get().isActive()) {
            throw new IllegalArgumentException("Couldn't find your subscription");
        }

        // plano é válido e está ativo
        String newPlan = requestBody.get("planName");

        Optional<Plan> plan = planRepository.findById(newPlan);

        if(plan.isEmpty() || !plan.get().isActive()) {
            throw new IllegalArgumentException("The plan with name '" + newPlan + "' either doesn't exist or is no longer active.");
        }

        // plano atual
        if(!subscription.get().getPlanName().equals(newPlan)) {
            subscription.get().setPlanName(newPlan);
            subscriptionRepository.save(subscription.get());

            SubscriptionDTO subscriptionDTO = subscriptionMapper.toSubscriptionDTO(subscription.get());
            sender.publishPlanChanged(subscriptionDTO);

            return subscriptionDTO;
        }
        throw new IllegalArgumentException("Your subscription is already of type " + newPlan);
    }

    public Iterable<Subscription> findAll() {

        Stream<Subscription> localSubscriptions = StreamSupport.stream(subscriptionRepository.findAll().spliterator(), false);

        return localSubscriptions.collect(Collectors.toList());
    }

    public Optional<Subscription> findOne(final UUID subscriptionId) {

        return Optional.ofNullable(subscriptionRepository.findBySubscriptionId(subscriptionId));
    }

    @Override
    public boolean createBonusSub(BonusPlanRequest request){
        final var isUserValid = subscriptionRepository.findNonBonusActiveSubscriptions(UUID.fromString(request.getUserId()));
        if (isUserValid.isPresent()) {
            final var isUserBonusValid = subscriptionRepository.findBonusActiveSubscriptions(UUID.fromString(request.getUserId()));
            if (isUserBonusValid.isEmpty()) {
                Subscription newBonusSub = new Subscription(request.getPlanName(), true, UUID.fromString(request.getUserId()));
                System.out.println("Bonus Sub " + newBonusSub.getSubscriptionId() + " created");
                subscriptionRepository.save(newBonusSub);
                sender.publishSubscriptionCreated(newBonusSub);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}

