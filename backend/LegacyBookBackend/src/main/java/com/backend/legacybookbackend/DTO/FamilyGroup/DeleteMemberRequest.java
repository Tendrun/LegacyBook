package com.backend.legacybookbackend.DTO.FamilyGroup;

public class DeleteMemberRequest {
    private String userEmailToDelete;
    private long groupId;
    public long getGroupId(){ return groupId; }

    public String getUserEmailToDelete() { return userEmailToDelete; }
    public void setUserEmailToDelete(String userEmailToDelete ) { this.userEmailToDelete  = userEmailToDelete ; }

    @Override
    public String toString() {
        return "DeleteMemberRequest{" +
                "userEmailToDelete='" + userEmailToDelete + '\'' +
                ", groupId=" + groupId +
                '}';
    }
}
