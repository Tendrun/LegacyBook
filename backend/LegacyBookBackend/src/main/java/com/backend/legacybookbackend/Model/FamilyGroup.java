package com.backend.legacybookbackend.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // Adnotacja JPA oznaczająca, że klasa jest encją i będzie mapowana na tabelę w bazie danych
@Table(name = "FamilyGroup") // Nazwa tabeli w bazie danych
public class FamilyGroup {

    @Id // Klucz główny
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatyczne generowanie ID przez bazę danych
    private Long id;

    private String familyName; // Nazwa rodziny (grupy)

    public FamilyGroup() {} // Konstruktor domyślny wymagany przez JPA

    public FamilyGroup(String familyName) {
        this.familyName = familyName;
    }

    // Relacja jeden-do-wielu z UserGroupMembership (grupa ma wielu członków)
    @OneToMany(mappedBy = "familyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroupMembership> memberships = new ArrayList<>();

    // Getter ID grupy
    public Long getId() { return id; }

    // Getter nazwy rodziny
    public String getFamilyName() { return familyName; }

    // Getter listy członków grupy
    @OneToMany(mappedBy = "familyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<UserGroupMembership> getMemberships(){
        return memberships;
    }

    // Setter nazwy rodziny
    public void setFamilyName(String familyName) { this.familyName = familyName; }

    // Metoda pomocnicza do debugowania/drukowania obiektów
    @Override
    public String toString() {
        return "Group{id=" + id + ", familyName='" + familyName + "'}";
    }
}
