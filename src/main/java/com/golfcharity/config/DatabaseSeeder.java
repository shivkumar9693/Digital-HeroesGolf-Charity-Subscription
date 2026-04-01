package com.golfcharity.config;

import com.golfcharity.entity.Role;
import com.golfcharity.entity.User;
import com.golfcharity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "shivkumarthakur9693@gmail.com";
        
        // Safety Patch: Unban all existing legacy users caused by the recent boolean 'enabled' schema addition
        userRepository.findAll().forEach(user -> {
            if (!user.isEnabled()) {
                user.setEnabled(true);
                userRepository.save(user);
            }
        });
        
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("12345"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("Default Admin Account Created: " + adminEmail);
        }
    }
}
