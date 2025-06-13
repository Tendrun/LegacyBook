package com.backend.legacybookbackend.Model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Encja reprezentująca użytkownika systemu.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Unikalny identyfikator użytkownika.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Imię użytkownika.
     */
    private String name;

    /**
     * Unikalny adres e-mail użytkownika.
     */
    @Column(unique = true)
    private String email;

    /**
     * Zaszyfrowane hasło użytkownika.
     */
    private String password;

    /**
     * Ścieżka lub URL do zdjęcia profilowego użytkownika.
     */
    private String profilePicture;

    /**
     * Lista powiązań użytkownika z grupami rodzinnymi.
     * Mapowanie odwrotne na encję UserGroupMembership.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroupMembership> memberships;

    /**
     * Konstruktor bezargumentowy wymagany przez JPA.
     */
    public User() {}

    /**
     * Konstruktor tworzący nowego użytkownika z podanymi danymi.
     * @param name imię użytkownika
     * @param email unikalny adres e-mail użytkownika
     * @param password zaszyfrowane hasło użytkownika
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * Pobiera unikalny identyfikator użytkownika.
     * @return id użytkownika
     */
    public Long getId() {
        return id;
    }

    /**
     * Pobiera imię użytkownika.
     * @return imię użytkownika
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia imię użytkownika.
     * @param name imię użytkownika
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Pobiera adres e-mail użytkownika.
     * @return adres e-mail użytkownika
     */
    public String getEmail() {
        return email;
    }

    /**
     * Ustawia adres e-mail użytkownika.
     * @param email adres e-mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Pobiera hasło użytkownika (zaszyfrowane).
     * @return hasło użytkownika
     */
    public String getPassword() {
        return password;
    }

    /**
     * Ustawia hasło użytkownika (powinno być zaszyfrowane przed wywołaniem).
     * @param password hasło użytkownika
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Pobiera listę członkostw użytkownika w grupach rodzinnych.
     * @return lista członkostw (UserGroupMembership)
     */
    public List<UserGroupMembership> getMemberships() {
        return memberships;
    }

    /**
     * Ustawia listę członkostw użytkownika w grupach rodzinnych.
     * @param memberships lista członkostw
     */
    public void setMemberships(List<UserGroupMembership> memberships) {
        this.memberships = memberships;
    }

    /**
     * Pobiera ścieżkę lub URL do zdjęcia profilowego użytkownika.
     * @return ścieżka do zdjęcia profilowego
     */
    public String getProfilePicture() {
        return profilePicture;
    }

    /**
     * Ustawia ścieżkę lub URL do zdjęcia profilowego użytkownika.
     * @param profilePicture ścieżka do zdjęcia profilowego
     */
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
