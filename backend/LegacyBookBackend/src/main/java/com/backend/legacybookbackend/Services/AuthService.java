package com.backend.legacybookbackend.Services;

import com.backend.legacybookbackend.DTO.AuthResponse;
import com.backend.legacybookbackend.DTO.LoginRequest;
import com.backend.legacybookbackend.DTO.RegisterRequest;
import com.backend.legacybookbackend.Model.User;
import com.backend.legacybookbackend.Model.UserRepository;
import com.backend.legacybookbackend.Utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Konstruktor wstrzykujący potrzebne zależności przez Springa
    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Metoda rejestrująca nowego użytkownika i zwracająca JWT
    public String register(RegisterRequest request) {
        // Sprawdzenie, czy email jest już zarejestrowany
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        // Utworzenie nowego użytkownika z zahashowanym hasłem
        User user = new User(request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
        // Zapis użytkownika w bazie danych
        userRepo.save(user);
        // Wygenerowanie i zwrócenie tokenu JWT na podstawie emaila użytkownika
        return jwtUtil.generateToken(user.getEmail());
    }

    // Metoda logowania użytkownika i zwracania odpowiedzi z tokenem i nazwą użytkownika
    public AuthResponse login(LoginRequest request) {
        // Znalezienie użytkownika po emailu lub rzucenie wyjątku, jeśli nie znaleziono
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Sprawdzenie zgodności hasła (podanego vs. zahashowanego w bazie)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        // Wygenerowanie tokenu JWT dla użytkownika
        String token = jwtUtil.generateToken(user.getEmail());
        // Zwrócenie tokenu oraz nazwy użytkownika w obiekcie odpowiedzi
        return new AuthResponse(token, user.getName());
    }
}
