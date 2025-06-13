package com.backend.legacybookbackend.Services;

import com.backend.legacybookbackend.Model.User;
import com.backend.legacybookbackend.Model.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implementacja interfejsu {@link UserDetailsService}, używana przez Spring Security do
 * ładowania szczegółów użytkownika na podstawie adresu e-mail.
 * <p>
 * Klasa ta integruje się z repozytorium użytkowników {@link UserRepository},
 * aby odnaleźć użytkownika w bazie danych i dostarczyć dane uwierzytelniające
 * wymagane przez framework Spring Security.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;

    /**
     * Konstruktor klasy {@code UserDetailsServiceImpl}, przyjmujący instancję repozytorium użytkownika.
     *
     * @param userRepo repozytorium użytkownika służące do wyszukiwania danych użytkowników w bazie
     */
    public UserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Ładuje użytkownika na podstawie adresu e-mail (pełniącego funkcję nazwy użytkownika).
     * <p>
     * Metoda ta jest wywoływana przez mechanizm uwierzytelniania Spring Security
     * w celu pobrania danych logowania użytkownika.
     *
     * @param email adres e-mail użytkownika, który próbuje się zalogować
     * @return instancja {@link UserDetails} zawierająca e-mail, hasło i puste uprawnienia
     * @throws UsernameNotFoundException jeśli użytkownik o podanym adresie e-mail nie został odnaleziony
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Zwraca implementację UserDetails z adresem e-mail jako nazwą użytkownika,
        // zaszyfrowanym hasłem i pustą listą ról/uprawnień (Authorities)
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
