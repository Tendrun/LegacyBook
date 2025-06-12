package com.example.legacykeep.DTO;

import com.google.gson.annotations.SerializedName;

/**
 * Reprezentuje członkostwo użytkownika w grupie, zawierając informacje o roli,
 * roli rodzinnej, nazwie użytkownika oraz adresie e-mail.
 */
public class UserGroupMembership {

    /**
     * Rola użytkownika w grupie (np. administrator, członek).
     */
    @SerializedName("role")
    private String role;

    /**
     * Rola rodzinna użytkownika w grupie (np. Matka, Ojciec).
     */
    @SerializedName("familyRole")
    private FamilyRole familyRole;

    /**
     * Nazwa użytkownika.
     */
    @SerializedName("userName")
    private String userName;

    /**
     * Adres e-mail użytkownika.
     */
    @SerializedName("userEmail")
    private String userEmail;

    /**
     * Zwraca rolę użytkownika w grupie.
     * @return rola użytkownika
     */
    public String getRole() {
        return role;
    }

    /**
     * Ustawia rolę użytkownika w grupie.
     * @param role rola użytkownika
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Zwraca rolę rodzinną użytkownika.
     * @return rola rodzinna
     */
    public FamilyRole getFamilyRole() {
        return familyRole;
    }

    /**
     * Ustawia rolę rodzinną użytkownika.
     * @param familyRole rola rodzinna
     */
    public void setFamilyRole(FamilyRole familyRole) {
        this.familyRole = familyRole;
    }

    /**
     * Zwraca nazwę użytkownika.
     * @return nazwa użytkownika
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Ustawia nazwę użytkownika.
     * @param userName nazwa użytkownika
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Zwraca adres e-mail użytkownika.
     * @return adres e-mail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Ustawia adres e-mail użytkownika.
     * @param userEmail adres e-mail
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Typ wyliczeniowy reprezentujący możliwe role rodzinne użytkownika w grupie.
     */
    public enum FamilyRole {
        Sister,      // Siostra
        Brother,     // Brat
        Mother,      // Matka
        Father,      // Ojciec
        Grandfather, // Dziadek
        Grandma,     // Babcia
        None         // Brak roli rodzinnej
    }
}