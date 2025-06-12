package com.example.legacykeep.DTO;

/**
 * Reprezentuje żądanie ustawienia roli użytkownika w grupie.
 * Zawiera adres e-mail użytkownika, nową rolę oraz identyfikator grupy.
 */
public class SetRoleRequest {
    /**
     * Adres e-mail użytkownika, którego rola ma zostać zmieniona.
     */
    private String userEmailRole;

    /**
     * Nowa rola użytkownika w grupie.
     */
    private String role;

    /**
     * Identyfikator grupy.
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
    public String getRole() {
        return role;
    }

    /**
     * Ustawia nową rolę użytkownika w grupie.
     * @param role rola użytkownika
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Zwraca identyfikator grupy.
     * @return identyfikator grupy
     */
    public long getGroupId() {
        return groupId;
    }

    /**
     * Ustawia identyfikator grupy.
     * @param groupId identyfikator grupy
     */
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}