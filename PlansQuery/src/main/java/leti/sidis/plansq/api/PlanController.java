package leti.sidis.plansq.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import leti.sidis.plansq.exceptions.NotFoundException;
import leti.sidis.plansq.model.Plan;
import leti.sidis.plansq.model.Role;
import leti.sidis.plansq.services.PlanService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 *
 */
@Tag(name = "Plans", description = "Endpoints for managing plans")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")

public class PlanController {

    private final PlanService planService;

    private final PlanMapper planMapper;

    @Autowired  //TODO: Implementar roles
    private Helper helper;


/*
    @Operation(summary = "Creates a new plan")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed(Role.MARKETING_DIRECTOR)
    public ResponseEntity<PlanDTO> createPlan(
            final HttpServletRequest request,
            @Valid @RequestBody final CreatePlanRequest resource) {

        final var plan = planService.createPlan(resource);
        return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));
    }
*/

    @Operation(summary = "Gets all plans from the current instance")
    @GetMapping()
    public Iterable<PlanDTO> getAllPlans() {
        return planMapper.toDTOList(planService.findAll());
    }

  /*  @Operation(summary = "Gets all plans from other instances")
    @GetMapping("/external")
    public Iterable<PlanDTO> getAllPlansExternal() {
        return planMapper.toDTOList(planService.findAllExternal());
    }
*/
    @Operation(summary = "Gets a specific plan from the current instance")
    @GetMapping(value = "/{planName}")
    public ResponseEntity<PlanDTO> findById(@PathVariable("planName") final String planName) {

        final var plan = planService.findOne(planName).orElseThrow(() -> new NotFoundException(Plan.class, planName));

        return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));

    }
  /*  @Operation(summary = "Gets a specific plan from an other instance")
    @GetMapping(value = "/{planName}/external")
    public ResponseEntity<PlanDTO> findByIdExternal(@PathVariable("planName") final String planName) {

        final var plan = planService.findOneExternal(planName).orElseThrow(() -> new NotFoundException(Plan.class, planName));

        return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));

    }

    @Operation(summary = "Update a plan from the current instance")
    @PatchMapping(value = "/{planName}")
    @RolesAllowed(Role.MARKETING_DIRECTOR)
    public ResponseEntity<PlanDTO> update(
            HttpServletRequest request,
            @PathVariable("planName") @Parameter(description = "The name of the plan to update") final String planName,
            @Valid @RequestBody final EditPlanRequest resource)
    {
            final var plan = planService.update(planName, resource);
            return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));
    }

*//*    @Operation(summary = "Update a plan from another instance")
    @PatchMapping(value = "/{planName}/external")
    public ResponseEntity<PlanDTO> updateExternal(
            HttpServletRequest request,
            @PathVariable("planName") @Parameter(description = "The name of the plan to update") final String planName,
            @Valid @RequestBody final EditPlanRequest resource)
    {
        final var plan = planService.updateExternalPlan(planName, resource);
        return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));
    }*//*

    @Operation(summary = "Cease a plan from the current instance")
    @PatchMapping(value = "/{planName}/cease")
    @RolesAllowed(Role.MARKETING_DIRECTOR)
    public ResponseEntity<PlanDTO> ceasePlan(HttpServletRequest request,
                                             @PathVariable("planName") @Parameter(description = "The name of the plan to cease") final String planName)
    {
            final var plan = planService.cease(planName);

            return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));
    }
*/
/*    @Operation(summary = "Cease a plan from another instance")
    @PatchMapping(value = "/{planName}/cease/external")
    public ResponseEntity<PlanDTO> ceaseExternal(HttpServletRequest request,
                                                  @PathVariable("planName") @Parameter(description = "The name of the plan to cease") final String planName)
    {
        final var plan = planService.ceaseExternal(planName);
        return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));
    }*/


    /*
    @Operation(summary = "Deactivate a subscription")
    @PutMapping(value = "/{planName}/deactivate")
    //TODO: @RolesAllowed(Role.MARKETING_DIRECTOR)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PlanDTO> deactivatePlan(
            HttpServletRequest request,
            @PathVariable String planName
    ) {

            final Plan plan = planService.cancel(planName);

            final var newPlanUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(plan.getPlanName())
                    .build().toUri();

            return ResponseEntity.created(newPlanUri).eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));
    }

    @Operation(summary = "Update price of an existing plan")
    @PatchMapping(value = "/{planName}/updatePrice")
    //TODO: @RolesAllowed(Role.MARKETING_DIRECTOR)
    public ResponseEntity<PlanDTO> updatePrice(
            HttpServletRequest request,
            @PathVariable("planName") @Parameter(description = "The name of the plan to update") final String planName,
            @Valid @RequestBody final EditPriceRequest resource) {


        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=MARKETING_DIRECTOR)";
        if (firstRole.toString().equals(role)) {


        final var plan = planService.updatePrice(planName, resource, user.getUsername());
        return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only Marketing directors can update a plan!");
        }

    }


    @Operation(summary = "Promote a plan")
    @PutMapping(value = "/{planName}")
    //TODO: @RolesAllowed(Role.MARKETING_DIRECTOR)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PlanDTO> promotePlan(
            HttpServletRequest request,
            @PathVariable ("planName") @Parameter (description = "The name does not exist") final String planName) {
        long Id = helper.getUserByToken(request);

        User user = userRepository.getById(Id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=MARKETING_DIRECTOR)";
        if (firstRole.toString().equals(role)) {

            final Plan plan = planService.promote(planName);
            return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));
        }

        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only marketing directors can promote a plan!");

        }
    }

    @Operation(summary = "Gets price change history of a plan")
    @GetMapping(value = "/{planName}")
    //TODO: @RolesAllowed(Role.MARKETING_DIRECTOR)
    public ResponseEntity<Iterable<PriceHistory>> getPriceChangeHistory(
            HttpServletRequest request,
            @PathVariable("planName") final String planName) {

        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();
        String role = "Role(authority=MARKETING_DIRECTOR)";
        if (firstRole.toString().equals(role)) {


            final Iterable<PriceHistory> priceHistory = planService.getPriceHistory(planName);

            //return ResponseEntity.ok().body(planMapper.toPriceHistoryDTO(priceHistory));
            return ResponseEntity.ok().body(priceHistory);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only Marketing directors can get a plan's price changing history!");
        }
        */

}



