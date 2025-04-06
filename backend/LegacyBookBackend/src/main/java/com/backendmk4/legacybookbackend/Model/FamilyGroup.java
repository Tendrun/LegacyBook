package com.backendmk4.legacybookbackend.Model;

import jakarta.persistence.*;

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

    @ManyToMany(mappedBy = "familyGroups")
    private List<User> users;

    public Long getId() { return id; }
    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Group{id=" + id + ", familyName='" + familyName + "'}";
    }
}
