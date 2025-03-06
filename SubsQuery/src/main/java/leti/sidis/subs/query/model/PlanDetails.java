package leti.sidis.subs.query.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "planDetails")
@Entity
public class PlanDetails {
    @Id
    private String planName;
    private String numberOfMinutes;
    private String planDescription;
    private int maxUsers;
    private int musicCollections;
    private String musicSuggestions;
    private double monthlyFee;
    private double annualFee;
    private boolean isActive;
}

