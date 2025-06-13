package com.backend.legacybookbackend.Security;

import com.backend.legacybookbackend.Filter.JwtAuthFilter;
import com.backend.legacybookbackend.Services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    // Konstruktor wstrzykujący zależności (JwtAuthFilter i UserDetailsServiceImpl)
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    // Główna konfiguracja zabezpieczeń HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Wyłączenie CSRF (np. dla API REST)
                .csrf(csrf -> csrf.disable())

                // Konfiguracja uprawnień do ścieżek
                .authorizeHttpRequests(auth -> auth
                        // Zezwól wszystkim na dostęp do endpointów logowania i rejestracji
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()

                        // Zezwól na dostęp do folderu ze zdjęciami profilowymi (statyczne zasoby)
                        .requestMatchers("/profile_pictures/**").permitAll()

                        // Zezwól na dostęp do postów (np. publiczne posty)
                        .requestMatchers("/posts/**").permitAll()

                        // Wszystkie pozostałe żądania wymagają uwierzytelnienia
                        .anyRequest().authenticated()
                )

                // Konfiguracja sesji: stateless bo korzystamy z JWT, nie przechowujemy sesji na serwerze
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Dodanie filtra JWT przed standardowym filtrem logowania
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean odpowiedzialny za kodowanie haseł (BCrypt - bezpieczny algorytm)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean do zarządzania procesem uwierzytelniania
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
