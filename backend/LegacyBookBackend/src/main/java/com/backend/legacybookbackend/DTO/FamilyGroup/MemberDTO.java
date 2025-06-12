package com.backend.legacybookbackend.DTO.FamilyGroup;

public class MemberDTO {
    private String role;
    private String familyRole;
    private String userName;
    private String userEmail;

    public MemberDTO(String role, String familyRole, String userName, String userEmail) {
        this.role = role;
        this.familyRole = familyRole;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getRole() {
        return role;
    }

    public String getFamilyRole() {
        return familyRole;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }
}


