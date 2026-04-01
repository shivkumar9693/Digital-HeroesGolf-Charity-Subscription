package com.golfcharity.controller;

import com.golfcharity.entity.SubscriptionStatus;
import com.golfcharity.service.EmailService;
import com.golfcharity.service.StripeService;
import com.golfcharity.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;
    private final SubscriptionService subscriptionService;
    private final EmailService emailService;

    @Value("${stripe.webhook-secret}")
    private String endpointSecret;

    @Value("${app.base-url}")
    private String baseUrl;
 
    @PostMapping("/subscribe")
    public String subscribe(@RequestParam("plan") String plan, Authentication authentication) {
        try {
            String successUrl = baseUrl + "/dashboard?payment_success";
            String cancelUrl = baseUrl + "/pricing";
            String email = authentication.getName();
            
            String checkoutUrl = stripeService.createCheckoutSession(email, plan, successUrl, cancelUrl);
            return "redirect:" + checkoutUrl;
        } catch (StripeException e) {
            e.printStackTrace();
            return "redirect:/pricing?error=stripe";
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event = null;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (Exception e) {
            System.err.println("Webhook signature failed for payload: " + payload);
            return ResponseEntity.badRequest().body("Webhook signature failed: " + e.getMessage());
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = dataObjectDeserializer.getObject().orElse(null);

        System.out.println("Received Stripe event: " + event.getType());

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) stripeObject;
            if (session != null && session.getCustomerDetails() != null) {
                String customerEmail = session.getCustomerDetails().getEmail();
                String stripeCustomerId = session.getCustomer();
                String stripeSubscriptionId = session.getSubscription();
                String plan = session.getMetadata().get("plan");
                
                System.out.println("Processing subscription for email: " + customerEmail + ", plan: " + plan);
                
                LocalDate renewalDate = "YEARLY".equalsIgnoreCase(plan) ? 
                    LocalDate.now().plusYears(1) : LocalDate.now().plusMonths(1);
                
                subscriptionService.createOrUpdateSubscription(customerEmail, stripeCustomerId, stripeSubscriptionId, plan, SubscriptionStatus.ACTIVE, renewalDate);
                emailService.sendPaymentSuccessEmail(customerEmail, plan);
            }
        }

        return ResponseEntity.ok("");
    }
}
