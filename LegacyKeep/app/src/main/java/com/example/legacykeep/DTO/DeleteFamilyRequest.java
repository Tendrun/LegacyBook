package com.example.legacykeep.DTO;

/**
 * Reprezentuje żądanie usunięcia rodziny.
 * Zawiera identyfikator grupy, która ma zostać usunięta.
 */
public class DeleteFamilyRequest {
    /**
     * Identyfikator grupy do usunięcia.
     */
    private long groupId;

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