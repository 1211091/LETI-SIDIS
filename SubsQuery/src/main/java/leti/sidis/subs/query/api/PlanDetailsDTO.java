package leti.sidis.subs.query.api;

import lombok.Data;

@Data
public class PlanDetailsDTO {
    private String planName;
    private String numberOfMinutes;
    private String planDescription;
    private int maxUsers;
    private int musicCollections;
    private String musicSuggestions;
    private double monthlyFee;
    private double annualFee;
}
