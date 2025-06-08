package com.example.legacykeep.DTO;

import com.google.gson.annotations.SerializedName;

public class UserGroupMembership {
    private long id;
    private String userEmail;
    private String userName;
    private String role;

    // Add any additional fields returned by the API
    @SerializedName("extraField")
    private String extraField; // Example field

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getExtraField() {
        return extraField;
    }

    public void setExtraField(String extraField) {
        this.extraField = extraField;
    }
}