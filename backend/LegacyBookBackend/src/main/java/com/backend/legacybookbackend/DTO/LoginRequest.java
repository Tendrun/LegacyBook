package com.backend.legacybookbackend.DTO;

public class LoginRequest {
    private String email;    // adres e-mail użytkownika
    private String password; // hasło użytkownika

    public String getEmail() {          // getter dla email
        return email;
    }
    public void setEmail(String email) { // setter dla email
        this.email = email;
    }

    public String getPassword() {         // getter dla hasła
        return password;
    }
    public void setPassword(String password) { // setter dla hasła
        this.password = password;
    }
}
