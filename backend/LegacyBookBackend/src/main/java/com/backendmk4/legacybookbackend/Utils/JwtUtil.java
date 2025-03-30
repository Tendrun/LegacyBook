package com.backendmk4.legacybookbackend.Utils;

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
            Path path = Paths.get("secret/secret.txt"); // in project root
            String secretLine = Files.readString(path).trim();
            SECRET = secretLine.split("=")[1].replaceAll("[\"; ]", "");
        }
        catch(IOException e) {
            System.out.println("Reading secret error");
        }
    }

    public String generateToken(String email) {
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

