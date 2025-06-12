package com.backend.legacybookbackend.Services;

import com.backend.legacybookbackend.Exception.FamilyGroupNotFoundException;
import com.backend.legacybookbackend.Exception.UserNotFoundException;
import com.backend.legacybookbackend.Model.*;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Serwis odpowiedzialny za zarządzanie członkostwem użytkowników w grupach rodzinnych.
 * <p>
 * Zapewnia funkcjonalności związane z rolami użytkowników w grupach rodzinnych,
 * w tym sprawdzanie dostępu na wysokim poziomie, ustawianie ról rodzinnych oraz ról użytkowników.
 */
@Service
public class UserGroupMembershipService {

    private final FamilyGroupRepository familyGroupRepository;
    private final UserRepository userRepository;
    private final UserGroupMembershipRepository userGroupMembershipRepository;

    /**
     * Konstruktor klasy `UserGroupMembershipService`.
     *
     * @param familyGroupRepository repozytorium grup rodzinnych
     * @param userRepository repozytorium użytkowników
     * @param userGroupMembershipRepository repozytorium członkostwa użytkowników w grupach
     */
    public UserGroupMembershipService(FamilyGroupRepository familyGroupRepository, UserRepository userRepository, UserGroupMembershipRepository userGroupMembershipRepository) {
        this.familyGroupRepository = familyGroupRepository;
        this.userRepository = userRepository;
        this.userGroupMembershipRepository = userGroupMembershipRepository;
    }

    /**
     * Sprawdza, czy użytkownik ma dostęp na wysokim poziomie (Admin lub Owner) w danej grupie rodzinnej.
     *
     * @param userEmail adres e-mail użytkownika
     * @param GroupID identyfikator grupy rodzinnej
     * @return true, jeśli użytkownik ma dostęp na wysokim poziomie; false w przeciwnym razie
     */
    public boolean hasHighLevelAccess(String userEmail, long GroupID) {
        Optional<User> existingUser = userRepository.findByEmail(userEmail);

        try {
            User userEntity = existingUser.get();
            long UserID = userEntity.getId();
            String UserRole = userGroupMembershipRepository.getRole(GroupID, UserID);

            System.out.println("UserRole = " + UserRole);

            if (Objects.equals(UserRole, "Admin") || Objects.equals(UserRole, "Owner"))
                return true;

        } catch (NoSuchElementException e) {
            return false;
        }

        return false;
    }

    /**
     * Ustawia rolę rodzinną użytkownika w danej grupie rodzinnej.
     *
     * @param userEmail adres e-mail użytkownika
     * @param groupId identyfikator grupy rodzinnej
     * @param familyRole nowa rola rodzinna użytkownika
     * @throws UserNotFoundException jeśli użytkownik nie zostanie znaleziony
     * @throws FamilyGroupNotFoundException jeśli grupa rodzinna nie zostanie znaleziona
     * @throws RuntimeException jeśli członkostwo użytkownika w grupie nie zostanie znalezione
     */
    public void setFamilyRole(String userEmail, long groupId, String familyRole) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        UserGroupMembership membership = userGroupMembershipRepository
                .findByUserAndFamilyGroup(user, familyGroup)
                .orElseThrow(() -> new RuntimeException("MembershipNotFound"));

        long userId = user.getId();
        userGroupMembershipRepository.updateFamilyRole(userId, groupId, familyRole);
    }

    /**
     * Ustawia rolę użytkownika w danej grupie rodzinnej.
     *
     * @param userEmail adres e-mail użytkownika
     * @param groupId identyfikator grupy rodzinnej
     * @param userRoleStr nowa rola użytkownika w formie tekstowej
     * @throws UserNotFoundException jeśli użytkownik nie zostanie znaleziony
     * @throws FamilyGroupNotFoundException jeśli grupa rodzinna nie zostanie znaleziona
     * @throws RuntimeException jeśli członkostwo użytkownika w grupie nie zostanie znalezione
     *                           lub jeśli podana rola jest nieprawidłowa
     */
    public void setRole(String userEmail, long groupId, String userRoleStr) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        UserGroupMembership membership = userGroupMembershipRepository
                .findByUserAndFamilyGroup(user, familyGroup)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        long userId = user.getId();

        // Walidacja i konwersja string → enum
        UserGroupMembership.Role roleEnum;
        try {
            roleEnum = UserGroupMembership.Role.valueOf(userRoleStr); // np. "Admin"
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + userRoleStr);
        }

        // Aktualizacja
        userGroupMembershipRepository.updateRole(userId, groupId, roleEnum.name());
    }
}