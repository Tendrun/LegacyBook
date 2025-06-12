package com.backend.legacybookbackend.Services;

import com.backend.legacybookbackend.DTO.AuthResponse;
import com.backend.legacybookbackend.DTO.LoginRequest;
import com.backend.legacybookbackend.DTO.RegisterRequest;
import com.backend.legacybookbackend.Model.User;
import com.backend.legacybookbackend.Model.UserRepository;
import com.backend.legacybookbackend.Utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serwis odpowiedzialny za autoryzację i rejestrację użytkowników.
 * Obsługuje proces logowania oraz tworzenia nowych kont użytkowników,
 * wraz z generowaniem tokenów JWT.
 */
@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Konstruktor klasy {@code AuthService}.
     *
     * @param userRepo repozytorium użytkowników
     * @param passwordEncoder komponent do kodowania haseł
     * @param jwtUtil narzędzie do generowania tokenów JWT
     */
    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Rejestruje nowego użytkownika, koduje jego hasło i zapisuje w bazie danych.
     * Jeśli adres e-mail jest już zajęty, rzuca wyjątek.
     *
     * @param request dane rejestracyjne użytkownika (imię, e-mail, hasło)
     * @return wygenerowany token JWT dla nowo zarejestrowanego użytkownika
     * @throws RuntimeException jeśli e-mail jest już zarejestrowany
     */
    public String register(RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        userRepo.save(user);

        return jwtUtil.generateToken(user.getEmail());
    }

    /**
     * Loguje użytkownika na podstawie danych logowania i zwraca token JWT.
     * Sprawdza poprawność hasła względem zakodowanego hasła w bazie.
     *
     * @param request dane logowania (e-mail i hasło)
     * @return odpowiedź uwierzytelniająca zawierająca token i nazwę użytkownika
     * @throws RuntimeException jeśli użytkownik nie istnieje lub dane logowania są nieprawidłowe
     */
    public AuthResponse login(LoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName());
    }
}
