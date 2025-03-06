package leti.sidis.subs.query.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateSubscriptionRequest extends EditSubscriptionRequest{
    //private Long userId;
    private String planName;
    private String paymentFrequency;
}
