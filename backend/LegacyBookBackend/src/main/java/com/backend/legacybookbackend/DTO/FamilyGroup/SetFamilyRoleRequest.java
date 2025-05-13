package com.backend.legacybookbackend.DTO.FamilyGroup;

public class SetFamilyRoleRequest {
    private String userEmailRole;
    private String familyRole;
    private long groupId;

    public long getGroupId() { return groupId; }
    public void setGroupId(long groupId) { this.groupId = groupId; }

    public String getUserEmailRole() { return userEmailRole; }
    public void setUserEmailRole(String userEmailRole) { this.userEmailRole = userEmailRole; }

    public String getFamilyRole() { return familyRole; }
    public void setFamilyRole(String familyRole) { this.familyRole = familyRole; }
}

