package com.backend.legacybookbackend.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Znajduje użytkownika na podstawie adresu email (opcjonalny, bo może nie istnieć)
    Optional<User> findByEmail(String email);

    // Sprawdza, czy istnieje użytkownik o podanym emailu
    boolean existsByEmail(String email);
}
