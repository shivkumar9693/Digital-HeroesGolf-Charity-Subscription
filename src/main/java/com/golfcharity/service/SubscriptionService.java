package com.golfcharity.service;

import com.golfcharity.entity.Subscription;
import com.golfcharity.entity.SubscriptionStatus;
import com.golfcharity.entity.User;
import com.golfcharity.entity.Role;
import com.golfcharity.repository.SubscriptionRepository;
import com.golfcharity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createOrUpdateSubscription(String userEmail, String stripeCustomerId, String stripeSubscriptionId, String plan, SubscriptionStatus status, LocalDate renewalDate) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        
        Subscription sub = subscriptionRepository.findByUser(user).orElse(new Subscription());
        sub.setUser(user);
        sub.setStripeCustomerId(stripeCustomerId);
        sub.setStripeSubscriptionId(stripeSubscriptionId);
        sub.setPlan(plan);
        sub.setStatus(status);
        sub.setRenewalDate(renewalDate);
        
        subscriptionRepository.save(sub);

        if (status == SubscriptionStatus.ACTIVE) {
            user.setRole(Role.SUBSCRIBER);
        } else {
            user.setRole(Role.PUBLIC_VISITOR);
        }
        userRepository.save(user);
    }

    public Subscription getUserSubscription(User user) {
        return subscriptionRepository.findByUser(user).orElse(null);
    }
}
