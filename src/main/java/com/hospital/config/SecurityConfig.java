package com.hospital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplified REST calls locally
                .authorizeHttpRequests(auth -> auth
                        // Public Frontend static files
                        .requestMatchers("/", "/index.html", "/app.js", "/style.css", "/favicon.ico").permitAll()
                        // Public auth paths
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // Public directory query paths
                        .requestMatchers(HttpMethod.GET, "/api/v1/getAllHospitals").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/getAllDoctors/**").permitAll()
                        .requestMatchers("/api/v1/doctors/search").permitAll()
                        // Authenticated client/admin booking actions
                        .requestMatchers("/api/v1/book/appointment").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers("/api/v1/appointment/details").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers("/api/v1/appointment/reschedule").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers("/api/v1/appointment/cancel").hasAnyRole("CLIENT", "ADMIN")
                        // Admin modifications & reports
                        .requestMatchers("/api/v1/addHospital").hasRole("ADMIN")
                        .requestMatchers("/api/v1/addDoctors/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/appointment/complete").hasRole("ADMIN")
                        .requestMatchers("/api/v1/reports/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/getAllAppointment").hasRole("ADMIN")
                        // Catch-all
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessHandler((req, res, auth) -> {
                            res.setStatus(200);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"message\":\"Logged out successfully\"}");
                        })
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
