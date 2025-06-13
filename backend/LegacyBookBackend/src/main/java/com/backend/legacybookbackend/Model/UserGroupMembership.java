package com.backend.legacybookbackend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_group_link") // Mapuje encję na tabelę "user_group_link"
public class UserGroupMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoinkrementowane ID
    private Long id;

    @ManyToOne
    private User user; // Relacja wiele do jednego - wiele członkostw do jednego użytkownika

    @ManyToOne
    FamilyGroup familyGroup; // Relacja wiele do jednego - wiele członkostw do jednej grupy rodzinnej

    // Role rodzinne, które mogą mieć członkowie grupy
    public enum FamilyRole {
        Sister,
        Brother,
        Mom,
        Father,
        Grandfather,
        Grandma,
        None
    }

    // Role związane z uprawnieniami w grupie
    public enum Role {
        Owner,
        Admin,
        User
    }

    @Enumerated(EnumType.STRING) // Zapis enumów jako tekst (np. "Owner", a nie 0,1,...)
    public Role role;

    @Enumerated(EnumType.STRING)
    FamilyRole familyRole;

    // Settery
    public void setUser(User user){
        this.user = user;
    }

    public void setRole(Role role){
        this.role = role;
    }

    public void setFamilyGroup(FamilyGroup familyGroup){
        this.familyGroup = familyGroup;
    }

    public void setFamilyRole(FamilyRole familyRole){
        this.familyRole = familyRole;
    }

    // Pomocnicze gettery pobierające dane z powiązanego użytkownika
    public String getUserName() {
        return user.getName();
    }

    public String getUserEmail() {
        return user.getEmail();
    }
}
