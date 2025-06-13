package com.example.legacykeep.DTO;

/**
 * Reprezentuje odpowiedź na żądanie rejestracji użytkownika.
 * Zawiera komunikat zwrotny z serwera.
 */
public class RegisterResponse {
    /**
     * Komunikat zwrotny z serwera po rejestracji.
     */
    private String message;

    /**
     * Zwraca komunikat zwrotny z serwera.
     * @return komunikat
     */
    public String getMessage() {
        return message;
    }

    /**
     * Ustawia komunikat zwrotny z serwera.
     * @param message komunikat
     */
    public void setMessage(String message) {
        this.message = message;
    }
}