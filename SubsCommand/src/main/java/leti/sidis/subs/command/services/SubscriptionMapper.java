package leti.sidis.subs.command.services;
import leti.sidis.subs.command.api.SubscriptionDTO;
import leti.sidis.subs.command.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper (componentModel = "spring")
public abstract class SubscriptionMapper {
    public abstract SubscriptionDTO toSubscriptionDTO(Subscription subscription);
    public abstract Iterable<SubscriptionDTO> toDTOList(Iterable<Subscription> subscriptions);
    public abstract Subscription toSubscription(SubscriptionDTO subscriptionDTO);
}
