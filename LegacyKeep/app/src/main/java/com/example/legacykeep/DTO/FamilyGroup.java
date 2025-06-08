package com.example.legacykeep.DTO;

import java.util.List;

public class FamilyGroup {
    private long id;
    private String familyName;
    private List<UserGroupMembership> memberships;

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
}