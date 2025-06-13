package com.backend.legacybookbackend.DTO.FamilyGroup;

/**
 * DTO reprezentujące żądanie usunięcia członka z grupy rodzinnej.
 */
public class DeleteMemberRequest {
    /**
     * Email użytkownika, który ma zostać usunięty z grupy.
     */
    private String userEmailToDelete;

    /**
     * Identyfikator grupy rodzinnej, z której ma zostać usunięty członek.
     */
    private long groupId;

    /**
     * Pobiera identyfikator grupy rodzinnej.
     * @return id grupy
     */
    public long getGroupId(){
        return groupId;
    }

    /**
     * Pobiera email użytkownika do usunięcia.
     * @return email użytkownika
     */
    public String getUserEmailToDelete() {
        return userEmailToDelete;
    }

    /**
     * Ustawia email użytkownika do usunięcia.
     * @param userEmailToDelete email użytkownika
     */
    public void setUserEmailToDelete(String userEmailToDelete ) {
        this.userEmailToDelete  = userEmailToDelete ;
    }

    @Override
    public String toString() {
        return "DeleteMemberRequest{" +
                "userEmailToDelete='" + userEmailToDelete + '\'' +
                ", groupId=" + groupId +
                '}';
    }
}
