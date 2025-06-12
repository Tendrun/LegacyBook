package com.backend.legacybookbackend.DTO;

/**
 * DTO reprezentujący odpowiedź po uwierzytelnieniu użytkownika.
 * Zawiera token JWT oraz nazwę użytkownika (opcjonalnie).
 */
public class AuthResponse {
    /**
     * Token JWT do autoryzacji.
     */
    private String token;

    /**
     * Nazwa użytkownika powiązana z tokenem.
     */
    private String username;

    /**
     * Konstruktor z tokenem i nazwą użytkownika.
     * @param token token JWT
     * @param username nazwa użytkownika
     */
    public AuthResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

    /**
     * Konstruktor z tokenem, bez nazwy użytkownika.
     * @param token token JWT
     */
    public AuthResponse(String token) {
        this.token = token;
        this.username = null;
    }

    /**
     * Pobiera token JWT.
     * @return token JWT
     */
    public String getToken() {
        return token;
    }

    /**
     * Ustawia token JWT.
     * @param token token JWT
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Pobiera nazwę użytkownika.
     * @return nazwa użytkownika
     */
    public String getUsername() {
        return username;
    }

    /**
     * Ustawia nazwę użytkownika.
     * @param username nazwa użytkownika
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
