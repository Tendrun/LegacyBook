package com.backend.legacybookbackend.DTO;

/**
 * DTO reprezentujący dane logowania użytkownika.
 */
public class LoginRequest {

    /**
     * Email użytkownika służący do logowania.
     */
    private String email;

    /**
     * Hasło użytkownika.
     */
    private String password;

    /**
     * Pobiera email użytkownika.
     * @return email użytkownika
     */
    public String getEmail() { return email; }

    /**
     * Ustawia email użytkownika.
     * @param email email użytkownika
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Pobiera hasło użytkownika.
     * @return hasło użytkownika
     */
    public String getPassword() { return password; }

    /**
     * Ustawia hasło użytkownika.
     * @param password hasło użytkownika
     */
    public void setPassword(String password) { this.password = password; }
}
