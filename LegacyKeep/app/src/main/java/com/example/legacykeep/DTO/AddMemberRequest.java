package com.example.legacykeep.DTO;


public class AddMemberRequest {
    private String userEmailToAdd;
    private long groupId;

    public String getUserEmailToAdd() {
        return userEmailToAdd;
    }

    public void setUserEmailToAdd(String userEmailToAdd) {
        this.userEmailToAdd = userEmailToAdd;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}