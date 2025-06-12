package com.backend.legacybookbackend.DTO.FamilyGroup;

/**
 * DTO reprezentujący podstawowe informacje o profilu użytkownika.
 */
public class UserProfileDTO {
    /**
     * Nazwa użytkownika.
     */
    private String name;

    /**
     * Email użytkownika.
     */
    private String email;

    /**
     * Ścieżka lub URL do zdjęcia profilowego użytkownika.
     */
    private String profilePicture;

    /**
     * Konstruktor tworzący profil użytkownika z podanymi danymi.
     * @param name nazwa użytkownika
     * @param email email użytkownika
     * @param profilePicture ścieżka lub URL do zdjęcia profilowego
     */
    public UserProfileDTO(String name, String email, String profilePicture) {
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    /**
     * Pobiera nazwę użytkownika.
     * @return nazwa użytkownika
     */
    public String getName() { return name; }

    /**
     * Pobiera email użytkownika.
     * @return email użytkownika
     */
    public String getEmail() { return email; }

    /**
     * Pobiera ścieżkę lub URL do zdjęcia profilowego.
     * @return zdjęcie profilowe użytkownika
     */
    public String getProfilePicture() { return profilePicture; }
}
