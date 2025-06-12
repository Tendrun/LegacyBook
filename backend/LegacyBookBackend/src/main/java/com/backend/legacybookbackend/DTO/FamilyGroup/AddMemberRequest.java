package com.backend.legacybookbackend.DTO.FamilyGroup;

/**
 * DTO reprezentujące żądanie dodania nowego członka do grupy rodzinnej.
 */
public class AddMemberRequest {
    /**
     * Email użytkownika, którego chcemy dodać do grupy.
     */
    private String UserEmailToAdd;

    /**
     * ID grupy rodzinnej, do której dodajemy członka.
     */
    private long groupId;

    /**
     * Pobiera ID grupy rodzinnej.
     * @return ID grupy rodzinnej
     */
    public long getGroupId(){
        return groupId;
    }

    /**
     * Pobiera email użytkownika do dodania.
     * @return email użytkownika
     */
    public String getUserEmailToAdd() {
        return UserEmailToAdd;
    }

    /**
     * Ustawia email użytkownika do dodania.
     * @param UserEmailToAdd email użytkownika
     */
    public void setUserEmailToAdd(String UserEmailToAdd) {
        this.UserEmailToAdd = UserEmailToAdd;
    }
}
