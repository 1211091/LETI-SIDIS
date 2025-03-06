package leti.sidis.plans.api;

import leti.sidis.plans.model.Plan;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public abstract class PlanMapper {

    public abstract PlanDTO toPlanDTO(Plan plan);
        public abstract Iterable<PlanDTO> toDTOList(Iterable<Plan> plans);

}
