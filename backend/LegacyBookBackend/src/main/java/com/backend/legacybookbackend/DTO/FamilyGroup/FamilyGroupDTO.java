package com.backend.legacybookbackend.DTO.FamilyGroup;

import java.util.List;

/**
 * DTO reprezentujący grupę rodzinną wraz z listą członków (membership).
 */
public class FamilyGroupDTO {
    /**
     * Unikalny identyfikator grupy rodzinnej.
     */
    private Long id;

    /**
     * Nazwa grupy rodzinnej.
     */
    private String familyName;

    /**
     * Lista członków grupy wraz z ich rolami.
     */
    private List<MemberDTO> memberships;

    /**
     * Konstruktor z wszystkimi polami.
     *
     * @param id Identyfikator grupy rodzinnej.
     * @param familyName Nazwa grupy rodzinnej.
     * @param memberships Lista członków grupy.
     */
    public FamilyGroupDTO(Long id, String familyName, List<MemberDTO> memberships) {
        this.id = id;
        this.familyName = familyName;
        this.memberships = memberships;
    }

    /**
     * Konstruktor domyślny.
     */
    public FamilyGroupDTO() {
    }

    /**
     * Pobiera identyfikator grupy rodzinnej.
     * @return identyfikator grupy
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia identyfikator grupy rodzinnej.
     * @param id identyfikator grupy
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Pobiera nazwę grupy rodzinnej.
     * @return nazwa grupy
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Ustawia nazwę grupy rodzinnej.
     * @param familyName nazwa grupy
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * Pobiera listę członków grupy rodzinnej.
     * @return lista członków
     */
    public List<MemberDTO> getMemberships() {
        return memberships;
    }

    /**
     * Ustawia listę członków grupy rodzinnej.
     * @param memberships lista członków
     */
    public void setMemberships(List<MemberDTO> memberships) {
        this.memberships = memberships;
    }
}
