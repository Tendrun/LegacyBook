package com.backend.legacybookbackend.DTO.FamilyGroup;

/**
 * DTO do ustawiania roli użytkownika w rodzinnej grupie.
 */
public class SetRoleRequest {
    /**
     * Email użytkownika, którego rola ma zostać ustawiona.
     */
    private String userEmailRole;

    /**
     * Nowa rola użytkownika.
     */
    private String Role;

    /**
     * Identyfikator grupy rodzinnej, w której ustawiana jest rola.
     */
    private long groupId;

    /**
     * Pobiera identyfikator grupy rodzinnej.
     * @return identyfikator grupy
     */
    public long getGroupId() { return groupId; }

    /**
     * Ustawia identyfikator grupy rodzinnej.
     * @param groupId identyfikator grupy
     */
    public void setGroupId(long groupId) { this.groupId = groupId; }

    /**
     * Pobiera email użytkownika, którego rola ma zostać ustawiona.
     * @return email użytkownika
     */
    public String getUserEmailRole() { return userEmailRole; }

    /**
     * Ustawia email użytkownika, którego rola ma zostać ustawiona.
     * @param userEmailRole email użytkownika
     */
    public void setUserEmailRole(String userEmailRole) { this.userEmailRole = userEmailRole; }

    /**
     * Pobiera nazwę roli użytkownika.
     * @return nazwa roli
     */
    public String getRole() { return Role; }

    /**
     * Ustawia nazwę roli użytkownika.
     * @param Role nazwa roli
     */
    public void setRole(String Role) { this.Role = Role; }
}
