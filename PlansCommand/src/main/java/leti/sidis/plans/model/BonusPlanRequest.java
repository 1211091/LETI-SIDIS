package leti.sidis.plans.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


public class BonusPlanRequest {

    @JsonProperty("userId")
    @Setter
    @Getter
    private String userId;

    @JsonProperty("planName")
    @Getter
    @Setter
    private String planName;

    public BonusPlanRequest(String userId, String planName) {
        this.userId = userId;
        this.planName = planName;
    }

    public BonusPlanRequest() {
    }
}
