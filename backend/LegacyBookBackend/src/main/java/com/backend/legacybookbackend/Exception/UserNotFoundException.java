package com.backend.legacybookbackend.Exception;

/**
 * Wyjątek rzucany, gdy użytkownik nie zostanie znaleziony.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Konstruktor wyjątku z wiadomością błędu.
     * @param message wiadomość błędu
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
