package com.example.legacykeep.DTO;

/**
 * Reprezentuje żądanie rejestracji użytkownika.
 * Zawiera imię, adres e-mail oraz hasło wymagane do utworzenia konta.
 */
public class RegisterRequest {
    /**
     * Imię użytkownika.
     */
    private String name;

    /**
     * Adres e-mail użytkownika.
     */
    private String email;

    /**
     * Hasło użytkownika.
     */
    private String password;

    /**
     * Zwraca imię użytkownika.
     * @return imię użytkownika
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia imię użytkownika.
     * @param name imię użytkownika
     */
    public void setName(String name) {
        this.name = name;
    }

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
     * @return hasło użytkownika
     */
    public String getPassword() {
        return password;
    }

    /**
     * Ustawia hasło użytkownika.
     * @param password hasło użytkownika
     */
    public void setPassword(String password) {
        this.password = password;
    }
}