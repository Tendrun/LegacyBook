package com.example.legacykeep.DTO;

public class AuthResponse {
    private String token;
    private String username; // Add this field

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() { // Add getter
        return username;
    }

    public void setUsername(String username) { // Add setter
        this.username = username;
    }
}