package com.backend.legacybookbackend.DTO.FamilyGroup;

public class SetRoleRequest {
    private String userEmailRole;
    private String Role;
    private long groupId;

    public long getGroupId() { return groupId; }
    public void setGroupId(long groupId) { this.groupId = groupId; }

    public String getUserEmailRole() { return userEmailRole; }
    public void setUserEmailRole(String userEmailRole) { this.userEmailRole = userEmailRole; }

    public String getRole() { return Role; }
    public void setRole(String Role) { this.Role = Role; }
}
