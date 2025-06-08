package com.example.legacykeep.DTO;

public class SetRoleRequest {
    private String userEmailRole;
    private String role;
    private long groupId;

    public String getUserEmailRole() {
        return userEmailRole;
    }

    public void setUserEmailRole(String userEmailRole) {
        this.userEmailRole = userEmailRole;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}