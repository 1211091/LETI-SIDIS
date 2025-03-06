package leti.sidis.plansq.services;

import leti.sidis.plansq.api.CreatePlanRequest;
import leti.sidis.plansq.model.Plan;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class EditPlanMapper {

    public abstract Plan create(CreatePlanRequest request);

    //public abstract void update(EditPlanRequest request, @MappingTarget Plan plan);
}

