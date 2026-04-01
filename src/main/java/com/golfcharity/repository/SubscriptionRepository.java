package com.golfcharity.repository;

import com.golfcharity.entity.Subscription;
import com.golfcharity.entity.SubscriptionStatus;
import com.golfcharity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUser(User user);
    Optional<Subscription> findByStripeCustomerId(String stripeCustomerId);
    List<Subscription> findByStatus(SubscriptionStatus status);
}
