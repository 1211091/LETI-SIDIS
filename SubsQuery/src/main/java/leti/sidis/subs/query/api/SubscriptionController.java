package leti.sidis.subs.query.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import leti.sidis.subs.query.exceptions.NotFoundException;
import leti.sidis.subs.query.model.Subscription;
import leti.sidis.subs.query.services.SubscriptionMapper;
import leti.sidis.subs.query.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@Tag (name = "Subscriptions", description = "Endpoints for managing user subscriptions")
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;
    private final Helper helper;

    @Operation(summary = "Gets all subscriptions")
    @GetMapping
    public Iterable<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionMapper.toDTOList(subscriptionService.findAll());
    }

    @Operation(summary = "Gets a specific subscription")
    @GetMapping(value = "/{subscriptionId}")
    public ResponseEntity<SubscriptionDTO> findById(@PathVariable("subscriptionId") final UUID subscriptionId) {

        final var subscription = subscriptionService.findOne(subscriptionId).orElseThrow(() -> new NotFoundException(Subscription.class, subscriptionId));

        return ResponseEntity.ok().eTag(Long.toString(subscription.getVersion())).body(subscriptionMapper.toSubscriptionDTO(subscription));
    }

    @Operation(summary = "Gets plan detail's from a specific subscription in the current instance")
    @GetMapping(value = "/{subscriptionId}/plan")
    //@RolesAllowed(Role.SUBSCRIBER) //TODO
    public ResponseEntity<PlanDetailsDTO> getAssociatedPlanDetails(@PathVariable("subscriptionId") final UUID subscriptionId) {

        final var planDetails = subscriptionService.getAssociatedPlanDetails(subscriptionId);
        if (planDetails != null) {
            return ResponseEntity.ok().body(subscriptionMapper.toPlanDetailsDTO(planDetails));
        } else {
            throw new NotFoundException(Subscription.class, subscriptionId);
        }
    }
}