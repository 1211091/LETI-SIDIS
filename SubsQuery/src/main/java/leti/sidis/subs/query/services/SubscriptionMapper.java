package leti.sidis.subs.query.services;
import leti.sidis.subs.query.api.PlanDetailsDTO;
import leti.sidis.subs.query.api.SubscriptionDTO;
import leti.sidis.subs.query.model.Plan;
import leti.sidis.subs.query.model.PlanDetails;
import leti.sidis.subs.query.model.Subscription;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper (componentModel = "spring")
public abstract class SubscriptionMapper {
    public abstract SubscriptionDTO toSubscriptionDTO(Subscription subscription);
    public abstract Iterable<SubscriptionDTO> toDTOList(Iterable<Subscription> subscriptions);
    public abstract Subscription toSubscription(SubscriptionDTO subscriptionDTO);
    public abstract PlanDetailsDTO toPlanDetailsDTO(Plan planDetails);
}