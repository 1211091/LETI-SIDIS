package leti.sidis.subs.command.repositories;

import leti.sidis.subs.command.model.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepositoryJPA extends CrudRepository<Subscription, UUID>, SubscriptionRepository {
    @Override
    Subscription findBySubscriptionId(UUID id);

    @Query("SELECT s FROM Subscription s WHERE s.userId = :id AND s.isActive = true AND s.isBonus = false")
    Optional<Subscription> findNonBonusActiveSubscriptions(@Param("id") UUID id);

    @Query("SELECT s FROM Subscription s WHERE s.userId = :id AND s.isActive = true AND s.isBonus = true")
    Optional<Subscription> findBonusActiveSubscriptions(@Param("id") UUID id);

    @Query("SELECT s FROM Subscription s WHERE planName (s.planName) = :planName")
    ArrayList<Subscription> findSubsWithSpecificPlan(@Param("planName") String planName);
    @Query("SELECT COUNT(s) FROM Subscription s WHERE MONTH(s.subscriptionDate) = :month")
    int countNewSubscriptionsByMonth(@Param ("month") int month); //conta o numero de subscrições de um mês
    @Query("SELECT COUNT(s) FROM Subscription s WHERE MONTH(s.cancellationDate) = :month")
    int countCancelledSubscriptionsByMonth(@Param ("month") int month); //conta o numero de cancelamentos de um mês

}
