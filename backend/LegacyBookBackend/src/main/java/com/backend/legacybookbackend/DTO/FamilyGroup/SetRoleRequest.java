package com.backend.legacybookbackend.DTO.FamilyGroup;

public class SetRoleRequest {
    private String userEmailRole; // email użytkownika, któremu nadajemy rolę
    private String Role;          // rola do nadania (np. Viewer, Editor)
    private long groupId;         // ID grupy, w której ustawiamy rolę

    public long getGroupId() {    // getter dla ID grupy
        return groupId;
    }
    public void setGroupId(long groupId) { // setter dla ID grupy
        this.groupId = groupId;
    }

    public String getUserEmailRole() { // getter dla emaila użytkownika
        return userEmailRole;
    }
    public void setUserEmailRole(String userEmailRole) { // setter dla emaila użytkownika
        this.userEmailRole = userEmailRole;
    }

    public String getRole() { // getter dla roli
        return Role;
    }
    public void setRole(String Role) { // setter dla roli
        this.Role = Role;
    }
}
