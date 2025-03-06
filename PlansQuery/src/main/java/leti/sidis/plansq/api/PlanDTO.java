package leti.sidis.plansq.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "A Plan")
public class PlanDTO {

    // business identity
    @Schema(description = "The name of the plan")
    private String planName;
    private String numberOfMinutes;
    private String planDescription;
    private int maxUsers;
    private int musicCollections;
    private String musicSuggestions;
    private double monthlyFee;
    private double annualFee;
    private boolean isActive;
    private boolean isPromoted;
}


