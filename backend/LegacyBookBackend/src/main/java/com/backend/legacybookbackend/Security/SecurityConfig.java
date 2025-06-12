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

/**
 * Konfiguracja bezpieczeństwa aplikacji.
 * <p>
 * Definiuje zasady autoryzacji i uwierzytelniania, w tym:
 * - wyłączenie CSRF (dla API REST)
 * - publiczne ścieżki (logowanie, rejestracja, reset hasła, zasoby statyczne)
 * - wymuszanie autoryzacji dla pozostałych endpointów
 * - bezstanową sesję (bez sesji HTTP)
 * - rejestrację filtra JWT do weryfikacji tokenów w nagłówkach żądań
 * - konfigurację enkodera haseł (BCrypt)
 * - dostarczenie menadżera uwierzytelniania
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Konstruktor klasy SecurityConfig.
     *
     * @param jwtAuthFilter filtr JWT do autoryzacji tokenów
     * @param userDetailsService implementacja serwisu UserDetailsService
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Konfiguruje łańcuch filtrów bezpieczeństwa HTTP.
     * <p>
     * Wyłącza CSRF, definiuje dostęp do endpointów, ustawia politykę sesji na stateless,
     * oraz dodaje filtr JWT przed filtrem uwierzytelniania użytkownika.
     *
     * @param http konfiguracja HttpSecurity
     * @return skonfigurowany łańcuch filtrów bezpieczeństwa
     * @throws Exception w przypadku błędów konfiguracji
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/resetPassword").permitAll()
                        .requestMatchers("/profile_pictures/**").permitAll()
                        .requestMatchers("/posts/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Definiuje bean enkodera haseł.
     * Używa BCrypt do bezpiecznego haszowania haseł.
     *
     * @return instancja PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Definiuje bean menadżera uwierzytelniania.
     * Pozwala na użycie domyślnej konfiguracji Spring Security do uwierzytelniania.
     *
     * @param config konfiguracja uwierzytelniania
     * @return instancja AuthenticationManager
     * @throws Exception w przypadku problemów z konfiguracją
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
