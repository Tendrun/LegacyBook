package com.backend.legacybookbackend.DTO.FamilyGroup;

/**
 * DTO reprezentujące żądanie utworzenia nowej grupy rodzinnej.
 */
public class CreateGroupRequest {
    /**
     * Nazwa nowej grupy rodzinnej.
     */
    private String familyName;

    /**
     * Pobiera nazwę grupy rodzinnej do utworzenia.
     * @return nazwa grupy rodzinnej
     */
    public String getFamilyName() {
        return familyName;
    }
}
