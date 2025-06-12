package com.example.legacykeep.DTO;


/**
 * Klasa DTO służąca do przekazywania danych podczas dodawania nowego członka do grupy rodzinnej.
 */
public class AddMemberRequest {
    /**
     * Adres e-mail użytkownika, który ma zostać dodany do grupy.
     */
    private String userEmailToAdd;

    /**
     * Identyfikator grupy rodzinnej, do której ma zostać dodany użytkownik.
     */
    private long groupId;

    /**
     * Zwraca adres e-mail użytkownika do dodania.
     * @return adres e-mail użytkownika
     */
    public String getUserEmailToAdd() {
        return userEmailToAdd;
    }

    /**
     * Ustawia adres e-mail użytkownika do dodania.
     * @param userEmailToAdd adres e-mail użytkownika
     */
    public void setUserEmailToAdd(String userEmailToAdd) {
        this.userEmailToAdd = userEmailToAdd;
    }

    /**
     * Zwraca identyfikator grupy rodzinnej.
     * @return identyfikator grupy
     */
    public long getGroupId() {
        return groupId;
    }

    /**
     * Ustawia identyfikator grupy rodzinnej.
     * @param groupId identyfikator grupy
     */
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}