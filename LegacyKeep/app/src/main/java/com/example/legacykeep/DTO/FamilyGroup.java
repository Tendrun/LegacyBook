package com.example.legacykeep.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FamilyGroup {
    private long id;
    private String familyName;
    private List<UserGroupMembership> memberships;

    // Add any additional fields returned by the API
    @SerializedName("extraField")
    private String extraField; // Example field

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public List<UserGroupMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<UserGroupMembership> memberships) {
        this.memberships = memberships;
    }

    public String getExtraField() {
        return extraField;
    }

    public void setExtraField(String extraField) {
        this.extraField = extraField;
    }
}