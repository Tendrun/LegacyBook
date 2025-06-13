package com.backend.legacybookbackend.Filter;

import com.backend.legacybookbackend.Services.UserDetailsServiceImpl;
import com.backend.legacybookbackend.Utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    // Konstruktor wstrzykujący zależności: narzędzie do obsługi JWT oraz serwis użytkownika
    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Pobierz nagłówek Authorization z żądania HTTP
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // Sprawdź, czy nagłówek istnieje i zaczyna się od "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Usuń prefiks "Bearer " i wyciągnij token JWT
            token = authHeader.substring(7);
            // Wydobądź adres email (czyli nazwę użytkownika) z tokena
            email = jwtUtil.extractUsername(token);
        }

        // Jeśli email został wyciągnięty i brak już ustawionego kontekstu uwierzytelnienia
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Wczytaj dane użytkownika na podstawie emaila
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Sprawdź, czy token jest ważny
            if (jwtUtil.validateToken(token)) {
                // Utwórz obiekt uwierzytelnienia Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Ustaw dodatkowe szczegóły (np. adres IP, sesję)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Ustaw uwierzytelnienie w kontekście bezpieczeństwa
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Kontynuuj filtrację (przekazanie żądania do kolejnych filtrów lub kontrolera)
        filterChain.doFilter(request, response);
    }
}
