package com.backend.legacybookbackend.Exception;

import org.springframework.http.HttpStatus;           // kody statusu HTTP
import org.springframework.http.ResponseEntity;     // reprezentacja odpowiedzi HTTP
import org.springframework.web.bind.annotation.*;    // adnotacje do obsługi wyjątków

@ControllerAdvice                                      // globalny handler wyjątków
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)    // obsługa UserNotFoundException
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)        // ustaw status 400 Bad Request
                .body(ex.getMessage());               // zwróć wiadomość wyjątku
    }

    @ExceptionHandler(FamilyGroupNotFoundException.class) // obsługa FamilyGroupNotFoundException
    public ResponseEntity<String> handleFamilyGroupNotFound(FamilyGroupNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)        // ustaw status 400 Bad Request
                .body(ex.getMessage());               // zwróć wiadomość wyjątku
    }
}

