package leti.sidis.plans.services;

import leti.sidis.plans.api.CreatePlanRequest;
import leti.sidis.plans.api.EditPlanRequest;
import leti.sidis.plans.model.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class EditPlanMapper {

    public abstract Plan create(CreatePlanRequest request);

    //public abstract void update(EditPlanRequest request, @MappingTarget Plan plan);
}

