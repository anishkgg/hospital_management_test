package com.hospital.config;

import com.hospital.entity.User;
import com.hospital.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed default Admin if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .build();
            userRepository.save(admin);
            System.out.println("[Database Seeder] Default Admin created: admin / admin123");
        }

        // Seed default Client if not exists
        if (!userRepository.existsByUsername("client")) {
            User client = User.builder()
                    .username("client")
                    .password(passwordEncoder.encode("client123"))
                    .role("ROLE_CLIENT")
                    .build();
            userRepository.save(client);
            System.out.println("[Database Seeder] Default Client created: client / client123");
        }
    }
}
