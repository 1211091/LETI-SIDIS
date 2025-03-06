package leti.sidis.subs.query.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Table(name = "subscriptions")
@Entity
public class Subscription{
    @Getter
    @Setter
    @Id
    private UUID subscriptionId;

    @Getter
    @Setter
    @NotBlank
    @NotNull(message = "A subscription name must be provided. I.e: 'Gold'")
    private String planName;

    @Getter
    @Transient
    private List<String> devices = new ArrayList<>();;

    @Getter
    @Version
    private long version;

    @Getter
    private LocalDate subscriptionDate;

    @Getter
    private LocalDate cancellationDate;

    @Getter
    private LocalDate lastRenovationDate;

    @Getter
    private int currentDevices = 0;

    @Getter
    @NotNull
    @NotBlank(message = "A payment frequency must be provided. I.e: 'Monthly'")
    private String paymentFrequency;

    private boolean isActive = true;

    @Getter
    @Setter
    private UUID userId;

    public Subscription(UUID subscriptionId, String planName, List<String> devices, LocalDate cancellationDate, LocalDate lastRenovationDate, int currentDevices, String paymentFrequency, boolean isActive, UUID userId) {
        setSubscriptionId(subscriptionId);
        setPlanName(planName);
        setDevices(devices);
        startSubscription();
        this.cancellationDate = cancellationDate;
        setLastRenovationDate(lastRenovationDate);
        setCurrentDevices(currentDevices);
        setPaymentFrequency(paymentFrequency);
        setActive(isActive);
        setUserId(userId);
    }

    public Subscription(final String planName, final UUID userId, final String paymentFrequency) {
        setPlanName(planName);
        setUserId(userId);
        setPaymentFrequency(paymentFrequency);
    }

    public Subscription(){}

    public void setDevices(List<String> devices) {
        this.devices = devices;
    }

    public void startSubscription() {
        this.subscriptionDate = LocalDate.now();
    }

    public void setCancellationDate() {
        this.cancellationDate = LocalDate.now();
    }

    public void setLastRenovationDate(LocalDate lastRenovationDate) {
        this.lastRenovationDate = LocalDate.now();
    }

    public void setCurrentDevices(int currentDevices) {
        this.currentDevices = currentDevices;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        if (Objects.equals(paymentFrequency, "Monthly") || Objects.equals(paymentFrequency, "Annually")) {
            this.paymentFrequency = paymentFrequency;
        }else{
            throw new IllegalArgumentException("Please choose a valid payment frequency!");
        }
    }
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionId=" + subscriptionId +
                ", planName='" + planName + '\'' +
                ", devices=" + devices +
                ", version=" + version +
                ", subscriptionDate=" + subscriptionDate +
                ", cancellationDate=" + cancellationDate +
                ", lastRenovationDate=" + lastRenovationDate +
                ", currentDevices=" + currentDevices +
                ", paymentFrequency='" + paymentFrequency + '\'' +
                ", isActive=" + isActive +
                ", userId=" + userId + '\'' +
                '}';
    }

    public Long calculateDate(LocalDate subscriptionDate, LocalDate cancellationDate){
        return ChronoUnit.DAYS.between(subscriptionDate, cancellationDate);
    }

    public void applyPatch(final String planName, final String paymentFrequency) {
        if (planName != null) {
            setPlanName(planName);
        }
        if (paymentFrequency != null) {
            setPaymentFrequency(paymentFrequency);
        }
    }

    public Subscription(final UUID subscriptionId){
        setSubscriptionId(subscriptionId);
    }

    public Subscription(final String planName){
        setPlanName(planName);
    }

    public Subscription(final LocalDate subscriptionDate){
        startSubscription();
    }

}
