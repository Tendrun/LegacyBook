package com.backend.legacybookbackend.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users") // Mapuje encję na tabelę "users" w bazie danych
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoinkrementowane ID
    private Long id;

    private String name;

    @Column(unique = true) // Email musi być unikalny
    private String email;

    private String password;

    private String profilePicture; // Ścieżka lub URL do zdjęcia profilowego

    // Relacja 1:N z UserGroupMembership — użytkownik może należeć do wielu grup
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroupMembership> memberships;

    public User() {} // Konstruktor bezargumentowy (wymagany przez JPA)

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Gettery i settery
    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public List<UserGroupMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<UserGroupMembership> memberships) {
        this.memberships = memberships;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
