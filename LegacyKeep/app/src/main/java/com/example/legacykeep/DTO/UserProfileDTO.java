package com.example.legacykeep.DTO;

/**
 * Reprezentuje profil użytkownika, zawierający podstawowe informacje
 * takie jak imię, adres e-mail oraz zdjęcie profilowe.
 */
public class UserProfileDTO {
    /**
     * Imię lub nazwa użytkownika.
     */
    private String name;

    /**
     * Adres e-mail użytkownika.
     */
    private String email;

    /**
     * Ścieżka lub URL do zdjęcia profilowego użytkownika.
     */
    private String profilePicture;

    /**
     * Tworzy nowy profil użytkownika z podanymi danymi.
     * @param name imię lub nazwa użytkownika
     * @param email adres e-mail użytkownika
     * @param profilePicture ścieżka lub URL do zdjęcia profilowego
     */
    public UserProfileDTO(String name, String email, String profilePicture) {
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    /**
     * Zwraca imię lub nazwę użytkownika.
     * @return imię lub nazwa użytkownika
     */
    public String getName() { return name; }

    /**
     * Zwraca adres e-mail użytkownika.
     * @return adres e-mail użytkownika
     */
    public String getEmail() { return email; }

    /**
     * Zwraca ścieżkę lub URL do zdjęcia profilowego użytkownika.
     * @return ścieżka lub URL do zdjęcia profilowego
     */
    public String getProfilePicture() { return profilePicture; }
}