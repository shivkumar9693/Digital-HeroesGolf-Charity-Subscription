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
        Thread.sleep(2000); // 🏁 Small delay to ensure DB tables are ready
        String adminEmail = "shivkumarthakur9693@gmail.com";
        // New Database: Set up the Default Admin account
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("12345"))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            System.out.println("New Database Initialized! Admin Created: " + adminEmail);
        }
    }
}
