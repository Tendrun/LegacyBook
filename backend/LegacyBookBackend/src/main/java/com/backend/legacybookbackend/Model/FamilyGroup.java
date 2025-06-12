package com.backend.legacybookbackend.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Encja reprezentująca grupę rodzinną.
 * Zawiera podstawowe informacje o grupie oraz listę członków powiązanych z tą grupą.
 */
@Entity
@Table(name = "FamilyGroup")
public class FamilyGroup {

    /**
     * Unikalny identyfikator grupy rodzinnej.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nazwa grupy rodzinnej.
     */
    private String familyName;

    /**
     * Lista członkostw użytkowników w tej grupie rodzinnej.
     */
    @OneToMany(mappedBy = "familyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroupMembership> memberships = new ArrayList<>();

    /**
     * Konstruktor domyślny.
     */
    public FamilyGroup() {}

    /**
     * Konstruktor tworzący grupę rodzinną o podanej nazwie.
     *
     * @param familyName nazwa grupy rodzinnej
     */
    public FamilyGroup(String familyName) {
        this.familyName = familyName;
    }

    /**
     * Zwraca identyfikator grupy rodzinnej.
     *
     * @return id grupy
     */
    public Long getId() {
        return id;
    }

    /**
     * Zwraca nazwę grupy rodzinnej.
     *
     * @return nazwa grupy
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Ustawia nazwę grupy rodzinnej.
     *
     * @param familyName nazwa grupy
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * Zwraca listę członków należących do tej grupy rodzinnej.
     *
     * @return lista członkostw użytkowników
     */
    public List<UserGroupMembership> getMemberships() {
        return memberships;
    }

    /**
     * Zwraca reprezentację tekstową grupy rodzinnej.
     *
     * @return string opisujący grupę
     */
    @Override
    public String toString() {
        return "Group{id=" + id + ", familyName='" + familyName + "'}";
    }
}
