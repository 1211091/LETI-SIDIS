package leti.sidis.subs.command.services;

import leti.sidis.subs.command.api.EditSubscriptionRequest;
import leti.sidis.subs.command.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Mapper(componentModel = "spring")
public abstract class EditSubscriptionMapper {

    public abstract Subscription create(EditSubscriptionRequest resource, UUID userId);

    public abstract void update(EditSubscriptionRequest request, @MappingTarget Subscription subscription);


}
