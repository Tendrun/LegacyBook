package com.backend.legacybookbackend.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Globalny handler wyjątków dla aplikacji.
 * Przechwytuje określone wyjątki i zwraca odpowiedni status HTTP oraz wiadomość.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Obsługuje wyjątek UserNotFoundException.
     *
     * @param ex wyjątek UserNotFoundException
     * @return odpowiedź HTTP z kodem 400 (BAD_REQUEST) oraz wiadomością błędu
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Obsługuje wyjątek FamilyGroupNotFoundException.
     *
     * @param ex wyjątek FamilyGroupNotFoundException
     * @return odpowiedź HTTP z kodem 400 (BAD_REQUEST) oraz wiadomością błędu
     */
    @ExceptionHandler(FamilyGroupNotFoundException.class)
    public ResponseEntity<String> handleFamilyGroupNotFound(FamilyGroupNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
