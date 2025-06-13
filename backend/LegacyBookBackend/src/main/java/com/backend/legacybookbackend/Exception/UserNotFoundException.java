package com.backend.legacybookbackend.Exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message); // przekazuje wiadomość do klasy RuntimeException
    }
}
