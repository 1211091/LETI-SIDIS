package leti.sidis.plansq.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.time.LocalDate;

@Table(name = "Plan")
@Entity
public class Plan {

    @Id
    @Column(unique = true)
    private String planName;
    @Column(nullable = false)
    private String numberOfMinutes;
    @NotBlank(message = "Please, insert a description")
    @Column(nullable = false)
    private String planDescription;
    @Column(nullable = false)
    private int maxUsers;
    @Column(nullable = false)
    private int musicCollections;
    @Column(nullable = false)
    private String musicSuggestions;
    @Column(nullable = false)
    private double monthlyFee;
    @Column(nullable = false)
    private double annualFee;

    private boolean isActive =  true;
    private boolean isPromoted;
    private boolean isBonus = false;
    @Version
    private long version;

    public boolean isBonus() {
        return isBonus;
    }

    public void setBonus(boolean bonus) {
        isBonus = bonus;
    }

    public Plan(String planName, String numberOfMinutes, String planDescription, int musicCollections, String musicSuggestions, double monthlyFee, double annualFee, boolean isBonus) {
        this.planName = planName;
        this.numberOfMinutes = numberOfMinutes;
        this.planDescription = planDescription;
        this.musicCollections = musicCollections;
        this.musicSuggestions = musicSuggestions;
        this.monthlyFee = monthlyFee;
        this.annualFee = annualFee;
        this.isBonus = isBonus;
    }

    public Plan() {
    }

    public Plan(String planName, String numberOfMinutes, String planDescription, int maxUsers, int musicCollections, String musicSuggestions, double monthlyFee, double annualFee, boolean isActive, boolean isPromoted, LocalDate lastPriceChangeDate, double previousMonthlyFee, double previousAnnualFee, double currentMonthlyFee, double currentAnnualFee) {
        this.planName = planName;
        this.numberOfMinutes = numberOfMinutes;
        this.planDescription = planDescription;
        this.maxUsers = maxUsers;
        this.musicCollections = musicCollections;
        this.musicSuggestions = musicSuggestions;
        this.monthlyFee = monthlyFee;
        this.annualFee = annualFee;
        this.isActive = isActive;
        this.isPromoted = isPromoted;
    }

    public Plan(final String planName) {
        setPlanName(planName);
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getNumberOfMinutes() {
        return numberOfMinutes;
    }

    public void setNumberOfMinutes(String numberOfMinutes) {
        this.numberOfMinutes = numberOfMinutes;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public int getMusicCollections() {
        return musicCollections;
    }

    public void setMusicCollections(int musicCollections) {
        this.musicCollections = musicCollections;
    }

    public String getMusicSuggestions() {
        return musicSuggestions;
    }

    public void setMusicSuggestions(String musicSuggestions) {
        this.musicSuggestions = musicSuggestions;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public double getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(double annualFee) {
        this.annualFee = annualFee;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isPromoted() {
        return isPromoted;
    }

    public void setPromoted(boolean promoted) {
        isPromoted = promoted;
    }

    public long getVersion() {return version;}


    public Plan update(final String numberOfMinutes, final String planDescription, final int maxUsers, final int musicCollections, final String musicSuggestions/*final double monthlyFee, final double annualFee */, final boolean isActive, final boolean isPromoted){
        setPlanDescription(planDescription);
        setNumberOfMinutes(numberOfMinutes);
        setMaxUsers(maxUsers);
        setMusicCollections(musicCollections);
        setMusicSuggestions(musicSuggestions);
        setActive(isActive);
        setPromoted(isPromoted);
        return this;
    }


    public void applyPrice(final double monthlyFee, final double annualFee) {
        if (monthlyFee != 0) {
            setMonthlyFee(monthlyFee);
        }
        if (annualFee != 0) {
            setAnnualFee(annualFee);
        }
    }


}
