package com.backend.legacybookbackend.Exception;

public class FamilyGroupNotFoundException extends RuntimeException {
    // wyjątek rzucany, gdy nie znaleziono grupy rodzinnej o podanym ID
    public FamilyGroupNotFoundException(String message) {
        super(message);
    }
}
