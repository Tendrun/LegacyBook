package com.example.legacykeep.DTO;

/**
 * Reprezentuje żądanie logowania użytkownika.
 * Zawiera adres e-mail oraz hasło wymagane do uwierzytelnienia.
 */
public class LoginRequest {
    /**
     * Adres e-mail użytkownika.
     */
    private String email;

    /**
     * Hasło użytkownika.
     */
    private String password;

    /**
     * Zwraca adres e-mail użytkownika.
     * @return adres e-mail
     */
    public String getEmail() {
        return email;
    }

    /**
     * Ustawia adres e-mail użytkownika.
     * @param email adres e-mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Zwraca hasło użytkownika.
     * @return hasło
     */
    public String getPassword() {
        return password;
    }

    /**
     * Ustawia hasło użytkownika.
     * @param password hasło
     */
    public void setPassword(String password) {
        this.password = password;
    }
}