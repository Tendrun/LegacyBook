package com.example.legacykeep.DTO;

import com.google.gson.annotations.SerializedName;

public class UserGroupMembership {

    @SerializedName("role")
    private String role;

    @SerializedName("familyRole")
    private FamilyRole familyRole;

    @SerializedName("userName")
    private String userName;

    @SerializedName("userEmail")
    private String userEmail;

    // Gettery i settery

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public FamilyRole getFamilyRole() {
        return familyRole;
    }

    public void setFamilyRole(FamilyRole familyRole) {
        this.familyRole = familyRole;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public enum FamilyRole {
        Sister,
        Brother,
        Mother,
        Father,
        Grandfather,
        Grandma,
        None
    }
}
