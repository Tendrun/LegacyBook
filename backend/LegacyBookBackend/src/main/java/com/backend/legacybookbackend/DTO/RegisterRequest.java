package com.backend.legacybookbackend.DTO;

public class RegisterRequest {
    private String name;     // imię i nazwisko użytkownika
    private String email;    // adres e-mail użytkownika
    private String password; // hasło do konta użytkownika

    public String getName() {          // getter dla pola name
        return name;
    }
    public void setName(String name) { // setter dla pola name
        this.name = name;
    }

    public String getEmail() {           // getter dla pola email
        return email;
    }
    public void setEmail(String email) { // setter dla pola email
        this.email = email;
    }

    public String getPassword() {           // getter dla pola password
        return password;
    }
    public void setPassword(String password) { // setter dla pola password
        this.password = password;
    }
}
