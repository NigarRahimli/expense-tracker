package com.codewithniki.expensetracker.config;

import com.codewithniki.expensetracker.model.entities.Role;
import com.codewithniki.expensetracker.model.entities.User;
import com.codewithniki.expensetracker.repositories.RoleRepository;
import com.codewithniki.expensetracker.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AdminProperties adminProperties) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminProperties = adminProperties;
    }

    @Override
    public void run(String... args) {

        // Create ROLE_USER if missing
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(createRole("ROLE_USER")));

        // Create ROLE_ADMIN if missing
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(createRole("ROLE_ADMIN")));

        // Create Admin user if missing
        String adminEmail = adminProperties.getEmail();

        if (adminEmail == null || adminEmail.isBlank()) {
            System.out.println("app.admin.email is empty. Skipping admin creation.");
            return;
        }

        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            User admin = new User();
            admin.setName(adminProperties.getName() != null ? adminProperties.getName() : "System Admin");
            admin.setEmail(adminEmail);

            String rawPassword = adminProperties.getPassword();
            if (rawPassword == null || rawPassword.isBlank()) {
                System.out.println(" app.admin.password is empty. Skipping admin creation.");
                return;
            }

            admin.setPassword(passwordEncoder.encode(rawPassword));
            admin.setEmailVerified(true);
            admin.setTwoFactorEnabled(false);

            admin.getRoles().add(userRole);
            admin.getRoles().add(adminRole);

            userRepository.save(admin);

            System.out.println("Admin user created from application.properties: " + adminEmail);
        }
    }

    private Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}