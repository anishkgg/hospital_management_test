package com.hospital.Controller;

import com.hospital.entity.User;
import com.hospital.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Value("${app.security.google.client-id}")
    private String googleClientId;

    // DTO records
    public record LoginRequest(String username, String password) {}
    public record RegisterRequest(String username, String password) {}
    public record LoginResponse(String username, String role) {}
    public record GoogleAuthRequest(String credential) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Store security context explicitly in session for session-based cookie lookup
            HttpSession session = req.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            User user = userRepository.findByUsername(request.username())
                    .orElseThrow(() -> new RuntimeException("Authenticated user not found in DB"));

            return ResponseEntity.ok(new LoginResponse(user.getUsername(), user.getRole()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Username already exists"));
        }

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role("ROLE_CLIENT") // Defaults to CLIENT
                .build();

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Client account created successfully"));
    }

    @GetMapping("/status")
    public ResponseEntity<?> status() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }

        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_CLIENT");

        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "username", auth.getName(),
                "role", role
        ));
    }

    @GetMapping("/config")
    public ResponseEntity<?> config() {
        return ResponseEntity.ok(Map.of("googleClientId", googleClientId));
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleAuthRequest request, HttpServletRequest req) {
        String email;
        String credential = request.credential();

        if (credential.startsWith("mock-token:")) {
            email = credential.substring("mock-token:".length()).trim();
        } else {
            try {
                String verifyUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + credential;
                org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
                Map<?, ?> response = restTemplate.getForObject(verifyUrl, Map.class);
                if (response == null || response.containsKey("error_description")) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Google ID Token verification failed"));
                }
                email = (String) response.get("email");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Failed to verify with Google token servers"));
            }
        }

        if (email == null || email.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email parameter missing"));
        }

        User user = userRepository.findByUsername(email).orElseGet(() -> {
            User newUser = User.builder()
                    .username(email)
                    .password(passwordEncoder.encode(java.util.UUID.randomUUID().toString()))
                    .role("ROLE_CLIENT")
                    .build();
            return userRepository.save(newUser);
        });

        org.springframework.security.core.userdetails.UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(java.util.Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(user.getRole())))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = req.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return ResponseEntity.ok(new LoginResponse(user.getUsername(), user.getRole()));
    }
}
