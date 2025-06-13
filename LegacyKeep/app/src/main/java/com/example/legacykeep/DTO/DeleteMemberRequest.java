package com.example.legacykeep.DTO;

/**
 * Reprezentuje żądanie usunięcia członka z grupy.
 * Zawiera adres e-mail użytkownika do usunięcia oraz identyfikator grupy.
 */
public class DeleteMemberRequest {
    /**
     * Adres e-mail użytkownika, który ma zostać usunięty.
     */
    private String userEmailToDelete;

    /**
     * Identyfikator grupy, z której użytkownik ma zostać usunięty.
     */
    private long groupId;

    /**
     * Zwraca adres e-mail użytkownika do usunięcia.
     *
     * @return adres e-mail jako {@code String}
     */
    public String getUserEmailToDelete() {
        return userEmailToDelete;
    }

    /**
     * Ustawia adres e-mail użytkownika do usunięcia.
     *
     * @param userEmailToDelete adres e-mail do ustawienia
     */
    public void setUserEmailToDelete(String userEmailToDelete) {
        this.userEmailToDelete = userEmailToDelete;
    }

    /**
     * Zwraca identyfikator grupy.
     *
     * @return identyfikator grupy jako {@code long}
     */
    public long getGroupId() {
        return groupId;
    }

    /**
     * Ustawia identyfikator grupy.
     *
     * @param groupId identyfikator grupy do ustawienia
     */
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}