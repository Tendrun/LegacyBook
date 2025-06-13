package com.backend.legacybookbackend.DTO.FamilyGroup;

public class DeleteMemberRequest {
    private String userEmailToDelete; // email użytkownika do usunięcia z grupy
    private long groupId;             // ID grupy, z której usuwamy użytkownika

    public long getGroupId() {        // getter dla ID grupy
        return groupId;
    }

    public String getUserEmailToDelete() { // getter dla emaila użytkownika
        return userEmailToDelete;
    }

    public void setUserEmailToDelete(String userEmailToDelete) { // setter dla emaila użytkownika
        this.userEmailToDelete = userEmailToDelete;
    }
}
