package com.example.legacykeep.DTO;

public class CreateGroupRequest {
    private String familyName;

    public CreateGroupRequest(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}