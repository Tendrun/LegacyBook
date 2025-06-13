package com.backend.legacybookbackend.Utils;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.nio.file.*;

/**
 * Klasa pomocnicza służąca do generowania i weryfikowania tokenów JWT.
 * <p>
 * Wykorzystuje bibliotekę JJWT do obsługi tokenów i odczytuje klucz tajny (SECRET)
 * z pliku `secret/secret.txt` podczas inicjalizacji komponentu.
 */
@Component
public class JwtUtil {

    // Statyczny klucz tajny używany do podpisywania i weryfikacji tokenów JWT
    private static String SECRET = "";

    /**
     * Metoda inicjalizująca wykonywana po utworzeniu komponentu Springa.
     * <p>
     * Odczytuje klucz tajny z pliku `secret/secret.txt`, w którym wartość powinna być w formacie `SECRET=wartość`.
     * Wymagana struktura katalogów oraz poprawny format pliku.
     *
     * @throws RuntimeException jeśli plik nie istnieje, ma zły format lub zawiera pusty klucz
     */
    @PostConstruct
    public void init() {
        try {
            Path path = Paths.get("secret/secret.txt");
            if (!Files.exists(path)) {
                throw new IOException("Plik secret.txt nie został znaleziony: " + path.toAbsolutePath());
            }

            String secretLine = Files.readString(path).trim();
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
     * Generuje token JWT dla podanego adresu e-mail.
     *
     * @param email adres e-mail użytkownika, który ma być zaszyty w tokenie
     * @return podpisany token JWT ważny przez 24 godziny
     * @throws IllegalStateException jeśli klucz SECRET nie został poprawnie zainicjalizowany
     */
    public String generateToken(String email) {
        if (SECRET == null || SECRET.isEmpty()) {
            throw new IllegalStateException("Klucz SECRET nie został zainicjalizowany");
        }

        return Jwts.builder()
                .setSubject(email) // dane użytkownika
                .setIssuedAt(new Date()) // data utworzenia
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 godziny
                .signWith(SignatureAlgorithm.HS256, SECRET) // podpisanie
                .compact();
    }

    /**
     * Odczytuje nazwę użytkownika (adres e-mail) z tokena JWT.
     *
     * @param token token JWT
     * @return adres e-mail użytkownika zawarty w tokenie
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Waliduje poprawność tokena JWT.
     *
     * @param token token JWT do sprawdzenia
     * @return true, jeśli token jest poprawny i podpis się zgadza; false w przeciwnym razie
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
