package com.backend.legacybookbackend.Utils;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.nio.file.*;


@Component
public class JwtUtil {
    private static String SECRET = "";


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

    public String generateToken(String email) {
        if (SECRET == null || SECRET.isEmpty()) {
            throw new IllegalStateException("Klucz SECRET nie został zainicjalizowany");
        }
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

