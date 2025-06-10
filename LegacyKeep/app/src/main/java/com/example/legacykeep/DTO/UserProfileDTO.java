package com.example.legacykeep.DTO;

public class UserProfileDTO {
    private String name;
    private String email;
    private String profilePicture;

    public UserProfileDTO(String name, String email, String profilePicture) {
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getProfilePicture() { return profilePicture; }
}