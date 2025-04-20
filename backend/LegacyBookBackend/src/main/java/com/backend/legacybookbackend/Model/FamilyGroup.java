package com.backend.legacybookbackend.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FamilyGroup")
public class FamilyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String familyName;

    public FamilyGroup() {}

    public FamilyGroup(String familyName) {
        this.familyName = familyName;
    }

    @OneToMany(mappedBy = "familyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroupMembership> memberships = new ArrayList<>();

    public Long getId() { return id; }
    public String getFamilyName() { return familyName; }
    public List<UserGroupMembership> getMemberships(){
        return memberships;
    }
    public void setFamilyName(String familyName) { this.familyName = familyName; }
    @Override
    public String toString() {
        return "Group{id=" + id + ", familyName='" + familyName + "'}";
    }
}
