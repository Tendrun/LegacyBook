package com.backend.legacybookbackend.Model;

import jakarta.persistence.*;

/**
 * Encja reprezentująca powiązanie użytkownika z grupą rodzinną,
 * wraz z przypisaną rolą w grupie oraz rolą rodzinną.
 */
@Entity
@Table(name = "user_group_link")
public class UserGroupMembership {

    /**
     * Unikalny identyfikator powiązania.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Użytkownik powiązany z grupą.
     */
    @ManyToOne
    private User user;

    /**
     * Grupa rodzinna, do której należy użytkownik.
     */
    @ManyToOne
    FamilyGroup familyGroup;

    /**
     * Definicja możliwych ról rodzinnych użytkownika w grupie.
     */
    public enum FamilyRole {
        Sister,
        Brother,
        Mother,
        Father,
        Grandfather,
        Grandma,
        None
    }

    /**
     * Definicja możliwych ról użytkownika w grupie.
     */
    public enum Role {
        Owner,
        Admin,
        User
    }

    /**
     * Rola użytkownika w grupie.
     */
    @Enumerated(EnumType.STRING)
    public Role role;

    /**
     * Rodzinna rola użytkownika w grupie.
     */
    @Enumerated(EnumType.STRING)
    public FamilyRole familyRole;

    /**
     * Ustawia użytkownika powiązanego z tym wpisem.
     * @param user użytkownik
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Ustawia rolę użytkownika w grupie.
     * @param role rola (Owner, Admin, User)
     */
    public void setRole(Role role){
        this.role = role;
    }

    /**
     * Ustawia grupę rodzinną powiązaną z tym wpisem.
     * @param familyGroup grupa rodzinna
     */
    public void setFamilyGroup(FamilyGroup familyGroup){
        this.familyGroup = familyGroup;
    }

    /**
     * Ustawia rodzinną rolę użytkownika.
     * @param familyRole rola rodzinna (np. Mother, Father, None)
     */
    public void setFamilyRole(FamilyRole familyRole){
        this.familyRole = familyRole;
    }

    /**
     * Pobiera nazwę użytkownika powiązanego z tym wpisem.
     * @return imię użytkownika
     */
    public String getUserName() {
        return user.getName();
    }

    /**
     * Pobiera adres e-mail użytkownika powiązanego z tym wpisem.
     * @return e-mail użytkownika
     */
    public String getUserEmail() {
        return user.getEmail();
    }
}
