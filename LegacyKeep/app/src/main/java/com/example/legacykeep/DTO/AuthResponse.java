package com.example.legacykeep.DTO;

/**
 * Reprezentuje odpowiedź uwierzytelniania zawierającą token oraz nazwę użytkownika.
 */
public class AuthResponse {
    /**
     * Token uwierzytelniający użytkownika.
     */
    private String token;

    /**
     * Nazwa użytkownika powiązana z tokenem.
     */
    private String username;

    /**
     * Zwraca token uwierzytelniający.
     *
     * @return token jako String
     */
    public String getToken() {
        return token;
    }

    /**
     * Ustawia token uwierzytelniający.
     *
     * @param token nowy token jako String
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Zwraca nazwę użytkownika.
     *
     * @return nazwa użytkownika jako String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Ustawia nazwę użytkownika.
     *
     * @param username nowa nazwa użytkownika jako String
     */
    public void setUsername(String username) {
        this.username = username;
    }
}