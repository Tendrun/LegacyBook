package com.backend.legacybookbackend.DTO;

/**
 * DTO reprezentujące dane potrzebne do rejestracji nowego użytkownika.
 */
public class RegisterRequest {

    /**
     * Imię użytkownika.
     */
    private String name;

    /**
     * Email użytkownika.
     */
    private String email;

    /**
     * Hasło użytkownika.
     */
    private String password;

    /**
     * Pobiera imię użytkownika.
     * @return imię użytkownika
     */
    public String getName() { return name; }

    /**
     * Ustawia imię użytkownika.
     * @param name imię użytkownika
     */
    public void setName(String name) { this.name = name; }

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
