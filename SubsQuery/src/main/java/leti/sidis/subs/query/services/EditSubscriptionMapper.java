package leti.sidis.subs.query.services;

import leti.sidis.subs.query.api.EditSubscriptionRequest;
import leti.sidis.subs.query.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class EditSubscriptionMapper {

    public abstract Subscription create(EditSubscriptionRequest resource);

    public abstract void update(EditSubscriptionRequest request, @MappingTarget Subscription subscription);
}
