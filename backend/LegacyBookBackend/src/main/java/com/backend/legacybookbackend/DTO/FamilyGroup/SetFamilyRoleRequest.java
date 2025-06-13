package com.backend.legacybookbackend.DTO.FamilyGroup;

public class SetFamilyRoleRequest {
    private String userEmailRole; // email użytkownika, któremu nadajemy rolę
    private String familyRole;     // nowa rola w grupie (np. Owner, Admin, Member)
    private long groupId;          // ID grupy, w której ustawiamy rolę

    public long getGroupId() {     // getter dla ID grupy
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

    public String getFamilyRole() { // getter dla roli rodzinnej
        return familyRole;
    }
    public void setFamilyRole(String familyRole) { // setter dla roli rodzinnej
        this.familyRole = familyRole;
    }
}
