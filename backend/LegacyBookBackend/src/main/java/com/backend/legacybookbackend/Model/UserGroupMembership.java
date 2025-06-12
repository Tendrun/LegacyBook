package com.backend.legacybookbackend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_group_link")
public class UserGroupMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne FamilyGroup familyGroup;

    public enum FamilyRole {
        Sister,
        Brother,
        Mother,
        Father,
        Grandfather,
        Grandma,
        None
    }

    public enum Role {
        Owner,
        Admin,
        User
    }
    @Enumerated(EnumType.STRING)
    public Role role;
    @Enumerated(EnumType.STRING)
    public FamilyRole familyRole;

    public void setUser(User user){
        this.user = user;
    }

    public void setRole(Role role){ this.role = role; }

    public void setFamilyGroup(FamilyGroup familyGroup){
        this.familyGroup = familyGroup;
    }

    public void setFamilyRole(FamilyRole familyRole){
        this.familyRole = familyRole;
    }

    public void getFamilyRole(FamilyRole familyRole){
        this.familyRole = familyRole;
    }

    public String getUserName() {
        return user.getName();
    }

    public String getUserEmail() {
        return user.getEmail();
    }
}
