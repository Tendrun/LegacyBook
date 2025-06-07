package com.example.legacykeep.DTO;

public class DeleteFamilyRequest {
    private long groupId;

    public DeleteFamilyRequest(long groupId) {
        this.groupId = groupId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}