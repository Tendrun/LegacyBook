package com.backend.legacybookbackend.Exception;

/**
 * Wyjątek rzucany, gdy grupa rodzinna nie zostanie znaleziona.
 */
public class FamilyGroupNotFoundException extends RuntimeException {
    /**
     * Konstruktor wyjątku z wiadomością błędu.
     * @param message wiadomość błędu
     */
    public FamilyGroupNotFoundException(String message) {
        super(message);
    }
}
