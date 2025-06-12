package com.backend.legacybookbackend.DTO.FamilyGroup;

/**
 * DTO do ustawiania roli rodzinnej użytkownika w grupie rodzinnej.
 */
public class SetFamilyRoleRequest {
    /**
     * Email użytkownika, którego rodzinna rola ma zostać ustawiona.
     */
    private String userEmailRole;

    /**
     * Nazwa roli rodzinnej, która ma zostać przypisana użytkownikowi.
     */
    private String familyRole;

    /**
     * Identyfikator grupy rodzinnej, w której ustawiana jest rola rodzinna.
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
     * Pobiera email użytkownika, którego rodzinna rola ma zostać ustawiona.
     * @return email użytkownika
     */
    public String getUserEmailRole() { return userEmailRole; }

    /**
     * Ustawia email użytkownika, którego rodzinna rola ma zostać ustawiona.
     * @param userEmailRole email użytkownika
     */
    public void setUserEmailRole(String userEmailRole) { this.userEmailRole = userEmailRole; }

    /**
     * Pobiera nazwę roli rodzinnej użytkownika.
     * @return nazwa roli rodzinnej
     */
    public String getFamilyRole() { return familyRole; }

    /**
     * Ustawia nazwę roli rodzinnej użytkownika.
     * @param familyRole nazwa roli rodzinnej
     */
    public void setFamilyRole(String familyRole) { this.familyRole = familyRole; }
}
