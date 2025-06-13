package com.example.legacykeep.DTO;

/**
 * Reprezentuje żądanie ustawienia roli użytkownika w grupie rodzinnej.
 * Zawiera adres e-mail użytkownika, nową rolę oraz identyfikator grupy.
 */
public class SetFamilyRoleRequest {
    /**
     * Adres e-mail użytkownika, którego rola ma zostać zmieniona.
     */
    private String userEmailRole;

    /**
     * Nowa rola użytkownika w grupie rodzinnej.
     */
    private String familyRole;

    /**
     * Identyfikator grupy rodzinnej.
     */
    private long groupId;

    /**
     * Zwraca adres e-mail użytkownika.
     * @return adres e-mail użytkownika
     */
    public String getUserEmailRole() {
        return userEmailRole;
    }

    /**
     * Ustawia adres e-mail użytkownika.
     * @param userEmailRole adres e-mail użytkownika
     */
    public void setUserEmailRole(String userEmailRole) {
        this.userEmailRole = userEmailRole;
    }

    /**
     * Zwraca nową rolę użytkownika w grupie.
     * @return rola użytkownika
     */
    public String getFamilyRole() {
        return familyRole;
    }

    /**
     * Ustawia nową rolę użytkownika w grupie.
     * @param familyRole rola użytkownika
     */
    public void setFamilyRole(String familyRole) {
        this.familyRole = familyRole;
    }

    /**
     * Zwraca identyfikator grupy rodzinnej.
     * @return identyfikator grupy
     */
    public long getGroupId() {
        return groupId;
    }

    /**
     * Ustawia identyfikator grupy rodzinnej.
     * @param groupId identyfikator grupy
     */
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}