package com.backend.legacybookbackend.DTO.FamilyGroup;

public class UserProfileDTO {
    private String name;            // imię i nazwisko użytkownika
    private String email;           // email użytkownika
    private String profilePicture;  // URL do zdjęcia profilowego

    public UserProfileDTO(String name, String email, String profilePicture) {
        this.name = name;                 // ustawienie pola name
        this.email = email;               // ustawienie pola email
        this.profilePicture = profilePicture; // ustawienie pola profilePicture
    }

    public String getName() {            // getter dla name
        return name;
    }

    public String getEmail() {           // getter dla email
        return email;
    }

    public String getProfilePicture() {  // getter dla profilePicture
        return profilePicture;
    }
}
