package com.backend.legacybookbackend.DTO.FamilyGroup;

/**
 * DTO reprezentujące żądanie usunięcia całej grupy rodzinnej.
 */
public class DeleteFamilyRequest {
    /**
     * Identyfikator grupy rodzinnej, która ma zostać usunięta.
     */
    private long groupId;

    /**
     * Pobiera identyfikator grupy rodzinnej do usunięcia.
     * @return id grupy rodzinnej
     */
    public long getGroupId() {
        return groupId;
    }
}
