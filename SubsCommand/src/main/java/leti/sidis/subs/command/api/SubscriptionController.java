package leti.sidis.subs.command.api;

import leti.sidis.subs.command.model.Role;
import leti.sidis.subs.command.services.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;


@Tag (name = "Subscriptions", description = "Endpoints for managing user subscriptions")
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final Helper helper;

    @Operation(summary = "Creates a subscription")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed(Role.NEW_CUSTOMER)
    public ResponseEntity<SubscriptionDTO> createSubscription(HttpServletRequest request,
                                                              @Valid @RequestBody final EditSubscriptionRequest resource)
    {
        UUID userId = helper.getUserByToken(request);

        final var subscriptionDTO = subscriptionService.create(resource, userId);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(subscriptionDTO.getSubscriptionId()).toUri()).body(subscriptionDTO);
    }

    @Operation(summary = "Renew subscription")
    @PatchMapping("/{subscriptionId}/renew")
    //@RolesAllowed(Role.SUBSCRIBER) //TODO
    public ResponseEntity<SubscriptionDTO> renewSubscription(@PathVariable("subscriptionId") final UUID subscriptionId,
                                                          HttpServletRequest request)
    {
        UUID userId = helper.getUserByToken(request);

        final var subscriptionDTO = subscriptionService.renewSubscription(subscriptionId);

        return ResponseEntity.ok().body(subscriptionDTO);
    }

    @Operation(summary = "Change plan")
    @PatchMapping("/{subscriptionId}/changePlan")
    //@RolesAllowed(Role.SUBSCRIBER) //TODO
    public ResponseEntity<SubscriptionDTO> changePlan(@PathVariable("subscriptionId") final UUID subscriptionId,
                                                   @RequestBody Map<String, String> requestBody,
                                                   HttpServletRequest request)
    {
        UUID userId = helper.getUserByToken(request);

        final var subscriptionDTO = subscriptionService.changePlan(subscriptionId, requestBody);

        return ResponseEntity.ok().body(subscriptionDTO);
    }

    @Operation(summary = "Cancel a subscription")
    @PatchMapping("/{subscriptionId}")
    @ResponseStatus(HttpStatus.CREATED)
    //@RolesAllowed(Role.SUBSCRIBER) //TODO
    public ResponseEntity<SubscriptionDTO> cancelSubscription(@PathVariable("subscriptionId") final UUID subscriptionId,
                                                              HttpServletRequest request)
    {
        UUID userId = helper.getUserByToken(request);

        final var subscriptionDTO = subscriptionService.cancel(subscriptionId);

        return ResponseEntity.ok().body(subscriptionDTO);
    }

}
