package com.example.legacykeep.model;

/**
 * Model reprezentujący członka rodziny w aplikacji.
 * Przechowuje adres e-mail, rolę oraz rolę w rodzinie.
 */
public class FamilyMemberModel {
    /**
     * Adres e-mail członka rodziny.
     */
    private String email;

    /**
     * Rola użytkownika (np. użytkownik, administrator).
     */
    private String role;

    /**
     * Rola w rodzinie (np. ojciec, matka, dziecko).
     */
    private String familyRole;

    /**
     * Tworzy nowy obiekt FamilyMemberModel.
     *
     * @param email      adres e-mail członka rodziny
     * @param role       rola użytkownika
     * @param familyRole rola w rodzinie
     */
    public FamilyMemberModel(String email, String role, String familyRole) {
        this.email = email;
        this.role = role;
        this.familyRole = familyRole;
    }

    /**
     * Zwraca adres e-mail członka rodziny.
     *
     * @return adres e-mail
     */
    public String getEmail() {
        return email;
    }

    /**
     * Zwraca rolę użytkownika.
     *
     * @return rola użytkownika
     */
    public String getRole() {
        return role;
    }

    /**
     * Zwraca rolę w rodzinie.
     *
     * @return rola w rodzinie
     */
    public String getFamilyRole() {
        return familyRole;
    }

    /**
     * Ustawia rolę w rodzinie.
     *
     * @param familyRole nowa rola w rodzinie
     */
    public void setFamilyRole(String familyRole) {
        this.familyRole = familyRole;
    }
}