package com.example.legacykeep.DTO;


public class DeleteMemberRequest {
    private String userEmailToDelete;
    private long groupId;

    public DeleteMemberRequest(String userEmailToDelete, long groupId) {
        this.userEmailToDelete = userEmailToDelete;
        this.groupId = groupId;
    }

    public String getUserEmailToDelete() {
        return userEmailToDelete;
    }

    public void setUserEmailToDelete(String userEmailToDelete) {
        this.userEmailToDelete = userEmailToDelete;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}