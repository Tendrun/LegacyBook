package com.backend.legacybookbackend.DTO.FamilyGroup;

public class AddMemberRequest {
    private String UserEmailToAdd;

    private long groupId;

    public long getGroupId(){ return groupId; }

    public String getUserEmailToAdd() { return UserEmailToAdd; }
    public void setUserEmailToAdd(String UserEmailToAdd) { this.UserEmailToAdd = UserEmailToAdd; }
}
