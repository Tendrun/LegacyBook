package com.backend.legacybookbackend.Utils;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Component
public class JwtUtil {

    // Tajny klucz do podpisu JWT, wczytywany z pliku
    private static String SECRET = "";

    /**
     * Metoda wywoływana po utworzeniu bean'a, wczytuje klucz SECRET z pliku secret.txt.
     * Oczekuje pliku w folderze "secret" w katalogu roboczym aplikacji.
     * Format pliku: np. SECRET=twoj_sekret
     */
    @PostConstruct
    public void init() {
        try {
            Path path = Paths.get("secret/secret.txt");
            if (!Files.exists(path)) {
                throw new IOException("Plik secret.txt nie został znaleziony: " + path.toAbsolutePath());
            }
            String secretLine = Files.readString(path).trim();

            // Sprawdzamy, czy format pliku jest poprawny (np. SECRET=xxx)
            if (secretLine.isEmpty() || !secretLine.contains("=")) {
                throw new IllegalArgumentException("Nieprawidłowy format pliku secret.txt");
            }

            SECRET = secretLine.split("=")[1].trim();

            if (SECRET.isEmpty()) {
                throw new IllegalArgumentException("Klucz SECRET nie może być pusty");
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Błąd inicjalizacji SECRET: " + e.getMessage());
            throw new RuntimeException("Nie można zainicjalizować klucza SECRET", e);
        }
    }

    /**
     * Generuje token JWT dla podanego emaila użytkownika.
     * Token ważny jest 24 godziny (86400000 ms).
     * @param email adres email użytkownika
     * @return wygenerowany token JWT
     */
    public String generateToken(String email) {
        if (SECRET == null || SECRET.isEmpty()) {
            throw new IllegalStateException("Klucz SECRET nie został zainicjalizowany");
        }
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h ważności tokena
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * Wyciąga nazwę użytkownika (email) z tokena JWT.
     * @param token token JWT
     * @return email użytkownika
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Sprawdza poprawność i ważność tokena JWT.
     * @param token token JWT
     * @return true jeśli token jest poprawny i ważny, false w przeciwnym wypadku
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Tu możesz dorzucić logowanie błędów jeśli chcesz śledzić problemy z tokenem
            return false;
        }
    }
}
