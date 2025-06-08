package com.example.legacykeep.DTO;

public class UserGroupMembership {
    private long id;
    private String userEmail;
    private String userName; // Add this field
    private String role;

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

    public String getUserName() { // Add getter
        return userName;
    }

    public void setUserName(String userName) { // Add setter
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}