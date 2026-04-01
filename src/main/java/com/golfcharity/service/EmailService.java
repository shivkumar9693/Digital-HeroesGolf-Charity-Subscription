package com.golfcharity.service;
 
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
 
import java.util.Map;
 
@Service
@RequiredArgsConstructor
public class EmailService {
 
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
 
    @org.springframework.beans.factory.annotation.Value("${app.base-url}")
    private String baseUrl;
 
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
 
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process("emails/" + templateName, context);
 
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom("no-reply@golfforgood.com");
 
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
 
    public void sendPaymentSuccessEmail(String to, String plan) {
        sendHtmlEmail(to, "Welcome to the Fairway! Subscription Confirmed", "payment-success", 
            Map.of("plan", plan, "userEmail", to, "baseUrl", baseUrl));
    }
 
    public void sendAccountBannedEmail(String to) {
        sendHtmlEmail(to, "Important Update Regarding Your GolfForGood Account", "account-banned", 
            Map.of("userEmail", to));
    }

    public void sendAccountUnbannedEmail(String to) {
        sendHtmlEmail(to, "Good News! Your GolfForGood Account is Active", "account-unbanned", 
            Map.of("userEmail", to));
    }
}
