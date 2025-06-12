package com.example.legacykeep.model;

public class FamilyMemberModel {
    private String email; // ← zamiast 'name'
    private String role;
    private String familyRole;

    public FamilyMemberModel(String email, String role, String familyRole) {
        this.email = email;
        this.role = role;
        this.familyRole = familyRole;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getFamilyRole() {
        return familyRole;
    }

    public void setFamilyRole(String familyRole) {
        this.familyRole = familyRole;
    }
}

