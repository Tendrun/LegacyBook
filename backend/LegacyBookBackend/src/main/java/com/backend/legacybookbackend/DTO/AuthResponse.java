package com.backend.legacybookbackend.DTO;

public class AuthResponse {
    private String token;    // JWT token zwracany klientowi
    private String username; // opcjonalna nazwa użytkownika

    public AuthResponse(String token, String username) {
        this.token = token;       // ustawienie tokenu
        this.username = username; // ustawienie nazwy użytkownika
    }

    public AuthResponse(String token) {
        this.token = token;       // ustawienie tokenu
        this.username = null;     // brak nazwy użytkownika
    }

    public String getToken() {    // getter dla tokenu
        return token;
    }

    public void setToken(String token) { // setter dla tokenu
        this.token = token;
    }

    public String getUsername() { // getter dla nazwy użytkownika
        return username;
    }

    public void setUsername(String username) { // setter dla nazwy użytkownika
        this.username = username;
    }
}
