package com.backend.legacybookbackend.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repozytorium JPA dla encji {@link User}.
 *
 * Udostępnia metody do operacji na danych użytkowników w bazie danych.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Znajduje użytkownika po adresie e-mail.
     *
     * @param email adres e-mail użytkownika
     * @return obiekt {@link Optional} zawierający użytkownika, jeśli istnieje,
     *         lub pusty Optional jeśli użytkownik o podanym e-mailu nie istnieje
     */
    Optional<User> findByEmail(String email);

    /**
     * Sprawdza, czy istnieje użytkownik o podanym adresie e-mail.
     *
     * @param email adres e-mail do sprawdzenia
     * @return {@code true}, jeśli użytkownik o podanym e-mailu istnieje,
     *         {@code false} w przeciwnym wypadku
     */
    boolean existsByEmail(String email);
}
