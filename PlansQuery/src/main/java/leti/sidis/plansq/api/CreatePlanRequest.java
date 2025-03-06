package leti.sidis.plansq.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreatePlanRequest extends EditPlanRequest {
 @NotNull
 @NotBlank(message = "A plan must have a name!")
 private String planName;
}

