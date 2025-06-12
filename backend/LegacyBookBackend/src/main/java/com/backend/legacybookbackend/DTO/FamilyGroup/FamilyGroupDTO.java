package com.backend.legacybookbackend.DTO.FamilyGroup;

import java.util.List;

public class FamilyGroupDTO {
    private Long id;
    private String familyName;
    private List<MemberDTO> memberships;

    public FamilyGroupDTO(Long id, String familyName, List<MemberDTO> memberships) {
        this.id = id;
        this.familyName = familyName;
        this.memberships = memberships;
    }

    public FamilyGroupDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public List<MemberDTO> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<MemberDTO> memberships) {
        this.memberships = memberships;
    }
}

