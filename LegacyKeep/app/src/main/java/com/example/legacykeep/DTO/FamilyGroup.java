package com.example.legacykeep.DTO;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Reprezentuje grupę rodzinną w aplikacji.
 * Zawiera identyfikator, nazwę rodziny, listę członkostw oraz dodatkowe pola zwracane przez API.
 */
public class FamilyGroup {
    /**
     * Unikalny identyfikator grupy rodzinnej.
     */
    private long id;

    /**
     * Nazwa rodziny.
     */
    private String familyName;

    /**
     * Lista członkostw użytkowników w grupie rodzinnej.
     */
    private List<UserGroupMembership> memberships;

    /**
     * Dodatkowe pole zwracane przez API.
     */
    @SerializedName("extraField")
    private String extraField;

    /**
     * Zwraca identyfikator grupy rodzinnej.
     * @return identyfikator grupy
     */
    public long getId() {
        return id;
    }

    /**
     * Ustawia identyfikator grupy rodzinnej.
     * @param id identyfikator grupy
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Zwraca nazwę rodziny.
     * @return nazwa rodziny
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Ustawia nazwę rodziny.
     * @param familyName nazwa rodziny
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * Zwraca listę członkostw użytkowników w grupie.
     * @return lista członkostw
     */
    public List<UserGroupMembership> getMemberships() {
        return memberships;
    }

    /**
     * Ustawia listę członkostw użytkowników w grupie.
     * @param memberships lista członkostw
     */
    public void setMemberships(List<UserGroupMembership> memberships) {
        this.memberships = memberships;
    }

    /**
     * Zwraca dodatkowe pole zwracane przez API.
     * @return dodatkowe pole
     */
    public String getExtraField() {
        return extraField;
    }

    /**
     * Ustawia dodatkowe pole zwracane przez API.
     * @param extraField dodatkowe pole
     */
    public void setExtraField(String extraField) {
        this.extraField = extraField;
    }
}