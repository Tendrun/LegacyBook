package com.example.legacykeep.model;

public class FamilyMemberModel {
    private String name;
    private String role;

    public FamilyMemberModel(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
