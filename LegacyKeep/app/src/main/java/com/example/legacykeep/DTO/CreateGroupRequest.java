package com.example.legacykeep.DTO;

/**
 * Klasa reprezentująca żądanie utworzenia nowej grupy rodzinnej.
 * Przekazuje nazwę rodziny, która ma zostać utworzona.
 * Używana do przesyłania danych z klienta do serwera podczas tworzenia grupy.
 */
public class CreateGroupRequest {
    /**
     * Nazwa rodziny, która ma zostać utworzona.
     */
    private String familyName;

    /**
     * Zwraca nazwę rodziny.
     *
     * @return nazwa rodziny jako String
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Ustawia nazwę rodziny.
     *
     * @param familyName nowa nazwa rodziny jako String
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}