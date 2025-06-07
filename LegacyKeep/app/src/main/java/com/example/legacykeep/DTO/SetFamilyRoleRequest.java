package com.example.legacykeep.DTO;

public class SetFamilyRoleRequest {
    private String userEmailRole;
    private String familyRole;
    private long groupId;

    public SetFamilyRoleRequest(String userEmailRole, String familyRole, long groupId) {
        this.userEmailRole = userEmailRole;
        this.familyRole = familyRole;
        this.groupId = groupId;
    }

    public String getUserEmailRole() {
        return userEmailRole;
    }

    public void setUserEmailRole(String userEmailRole) {
        this.userEmailRole = userEmailRole;
    }

    public String getFamilyRole() {
        return familyRole;
    }

    public void setFamilyRole(String familyRole) {
        this.familyRole = familyRole;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}