package com.backend.legacybookbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Główna klasa uruchomieniowa aplikacji Legacy Book Backend.
 * <p>
 * Aplikacja oparta na Spring Boot – ten komponent stanowi punkt wejścia
 * i inicjuje automatyczną konfigurację, skanowanie komponentów oraz uruchomienie kontekstu Springa.
 */
@SpringBootApplication // Adnotacja uruchamiająca automatyczną konfigurację i skanowanie komponentów
public class LegacyBookBackendApplication {

    /**
     * Metoda główna – punkt wejścia aplikacji Java.
     * <p>
     * Uruchamia aplikację Spring Boot, tworzy kontekst aplikacji i inicjuje wszystkie komponenty.
     *
     * @param args argumenty wiersza poleceń przekazywane przy starcie aplikacji (opcjonalne)
     */
    public static void main(String[] args) {
        // Uruchamia aplikację Spring Boot
        SpringApplication.run(LegacyBookBackendApplication.class, args);
    }

}
