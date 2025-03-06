package leti.sidis.plansq.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


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
