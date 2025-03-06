package leti.sidis.plans.api;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditPriceRequest {

    @Min(0)
    @NotNull
    private double monthlyFee;

    @Min(0)
    @NotNull
    private double annualFee;
}
