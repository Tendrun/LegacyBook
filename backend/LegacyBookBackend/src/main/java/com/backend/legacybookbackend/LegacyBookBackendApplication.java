package com.backend.legacybookbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Adnotacja oznaczająca główną klasę konfiguracyjną Spring Boot
@SpringBootApplication
public class LegacyBookBackendApplication {

    // Główna metoda uruchamiająca aplikację
    public static void main(String[] args) {
        // Uruchomienie aplikacji Spring Boot, która inicjalizuje kontekst Spring i serwer
        SpringApplication.run(LegacyBookBackendApplication.class, args);
    }

}
