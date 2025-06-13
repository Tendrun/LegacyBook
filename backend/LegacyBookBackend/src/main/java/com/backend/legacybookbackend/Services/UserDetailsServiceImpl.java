package com.backend.legacybookbackend.Services;

import com.backend.legacybookbackend.Model.User;
import com.backend.legacybookbackend.Model.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;

    // Konstruktor do wstrzykiwania repozytorium użytkowników
    public UserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // Metoda wykorzystywana przez Spring Security do ładowania użytkownika po emailu (loginie)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Szukamy użytkownika w bazie po emailu, jeśli nie ma, rzucamy wyjątek
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Tworzymy obiekt UserDetails z danymi użytkownika i pustą listą uprawnień (rol)
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),            // login (username)
                user.getPassword(),         // hasło (powinno być zaszyfrowane)
                Collections.emptyList()     // lista ról/authorities (tutaj pusta)
        );
    }
}
