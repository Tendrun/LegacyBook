package com.backend.legacybookbackend.DTO.FamilyGroup;

public class AddMemberRequest {
    private String UserEmailToAdd; // email użytkownika, którego chcemy dodać
    private long groupId;          // ID grupy, do której dodajemy użytkownika

    public long getGroupId() {     // getter dla groupId
        return groupId;
    }

    public String getUserEmailToAdd() { // getter dla adresu e-mail do dodania
        return UserEmailToAdd;
    }

    public void setUserEmailToAdd(String UserEmailToAdd) { // setter dla adresu e-mail
        this.UserEmailToAdd = UserEmailToAdd;
    }
}
