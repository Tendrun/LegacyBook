package com.backend.legacybookbackend.DTO.FamilyGroup;

/**
 * DTO reprezentujący członka grupy rodzinnej wraz z jego rolą oraz danymi użytkownika.
 */
public class MemberDTO {
    /**
     * Rola użytkownika w grupie (np. Owner, Admin, User).
     */
    private String role;

    /**
     * Rodzinna rola użytkownika (np. Sister, Brother, Mother).
     */
    private String familyRole;

    /**
     * Imię użytkownika.
     */
    private String userName;

    /**
     * Email użytkownika.
     */
    private String userEmail;

    /**
     * Konstruktor klasy MemberDTO.
     *
     * @param role Rola użytkownika w grupie.
     * @param familyRole Rodzinna rola użytkownika.
     * @param userName Imię użytkownika.
     * @param userEmail Email użytkownika.
     */
    public MemberDTO(String role, String familyRole, String userName, String userEmail) {
        this.role = role;
        this.familyRole = familyRole;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    /**
     * Pobiera rolę użytkownika w grupie.
     * @return rola użytkownika
     */
    public String getRole() {
        return role;
    }

    /**
     * Pobiera rodzinną rolę użytkownika.
     * @return rodzinna rola użytkownika
     */
    public String getFamilyRole() {
        return familyRole;
    }

    /**
     * Pobiera imię użytkownika.
     * @return imię użytkownika
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Pobiera email użytkownika.
     * @return email użytkownika
     */
    public String getUserEmail() {
        return userEmail;
    }
}
