package com.backend.legacybookbackend.Services;

import com.backend.legacybookbackend.Exception.FamilyGroupNotFoundException;
import com.backend.legacybookbackend.Exception.UserNotFoundException;
import com.backend.legacybookbackend.Model.*;
import com.backend.legacybookbackend.DTO.FamilyGroup.CreateGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class FamilyGroupService {

    private final FamilyGroupRepository familyGroupRepository;
    private final UserRepository userRepository;
    private final UserGroupMembershipRepository userGroupMembershipRepository;

    // Konstruktor wstrzykujący repozytoria potrzebne do operacji na bazie
    public FamilyGroupService(FamilyGroupRepository familyGroupRepository, UserRepository userRepository, UserGroupMembershipRepository userGroupMembershipRepository){
        this.familyGroupRepository = familyGroupRepository;
        this.userRepository = userRepository;
        this.userGroupMembershipRepository = userGroupMembershipRepository;
    }

    // Sprawdza czy użytkownik ma rolę Admin lub Owner w danej rodzinie (wysoki poziom dostępu)
    public boolean hasHighLevelAccess(String userEmail, long GroupID){
        Optional<User> existingUser = userRepository.findByEmail(userEmail);

        try {
            User userEntity = existingUser.get();
            long UserID = userEntity.getId();
            // Pobiera rolę użytkownika w rodzinie (np. Admin, Owner, User)
            String UserRole = userGroupMembershipRepository.getRole(GroupID, UserID);

            System.out.println("UserRole = " + UserRole);

            // Zwraca true jeśli rola to Admin lub Owner
            if(Objects.equals(UserRole, "Admin") || Objects.equals(UserRole, "Owner"))
                return true;

        } catch (NoSuchElementException e){
            // Jeśli użytkownik nie istnieje lub brak roli zwraca false
            return false;
        }

        return false;
    }

    // Sprawdza czy użytkownik jest właścicielem (Owner) rodziny
    public boolean UserIsFamilyOwner(String userEmail, long GroupID){
        Optional<User> existingUser = userRepository.findByEmail(userEmail);

        try {
            User userEntity = existingUser.get();
            long UserID = userEntity.getId();
            // Pobiera rolę użytkownika w rodzinie
            String UserRole = userGroupMembershipRepository.getRole(GroupID, UserID);

            System.out.println("UserRole = " + UserRole);

            // Zwraca true tylko jeśli rola to Owner
            if(Objects.equals(UserRole, "Owner"))
                return true;

        } catch (NoSuchElementException e){
            // W przypadku braku użytkownika lub roli - false
            return false;
        }

        return false;
    }

    // Usuwa rodzinę i powiązane z nią członkostwa w jednej transakcji
    @Transactional
    public void deleteFamily(long groupID){
        // Pobierz grupę po ID lub rzuć wyjątek jeśli nie istnieje
        FamilyGroup group = familyGroupRepository.findById(groupID)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Pobierz wszystkie powiązane memberships i usuń je ręcznie
        List<UserGroupMembership> memberships = userGroupMembershipRepository.findAllByFamilyGroup(group);
        userGroupMembershipRepository.deleteAll(memberships);

        // Usuń samą grupę
        familyGroupRepository.delete(group);
    }

    // Dodaj użytkownika do grupy rodzinnej z rolą User i domyślną rolą rodzinną None
    public void addMemberToFamily(String userEmail, long groupId) {
        // Znajdź użytkownika po emailu lub rzuć wyjątek
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Znajdź rodzinę po ID lub rzuć wyjątek
        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        // Utwórz nowe członkostwo
        UserGroupMembership membership = new UserGroupMembership();

        membership.setUser(user);
        membership.setFamilyGroup(familyGroup);
        membership.setRole(UserGroupMembership.Role.User); // standardowa rola
        membership.setFamilyRole(UserGroupMembership.FamilyRole.None); // brak roli rodzinnej

        // Zapisz do bazy
        userGroupMembershipRepository.save(membership);
    }

    // Usuwa użytkownika z grupy rodzinnej
    public void deleteMemberToFamily(String userEmail, long groupId) {
        // Znajdź użytkownika
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Znajdź rodzinę
        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        // Znajdź membership lub rzuć wyjątek jeśli nie ma
        UserGroupMembership membership = userGroupMembershipRepository
                .findByUserAndFamilyGroup(user, familyGroup)
                .orElseThrow(() -> new RuntimeException("MembershipNotFound"));

        // Usuń członkostwo
        userGroupMembershipRepository.delete(membership);
    }

    // Tworzy nową grupę rodzinną i przypisuje do niej użytkownika jako właściciela
    @Transactional
    public void createFamilyGroup(CreateGroupRequest request, String userEmail) {
        // Znajdź użytkownika
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Utwórz nową grupę
        FamilyGroup group = new FamilyGroup();
        group.setFamilyName(request.getFamilyName()); // ustaw nazwę grupy

        // Zapisz grupę do bazy
        familyGroupRepository.save(group);

        // Utwórz membership użytkownika jako Owner grupy
        UserGroupMembership membership = new UserGroupMembership();
        membership.setUser(user);
        membership.setFamilyGroup(group);
        membership.setFamilyRole(UserGroupMembership.FamilyRole.None); // brak roli rodzinnej na start
        membership.setRole(UserGroupMembership.Role.Owner); // właściciel grupy

        // Zapisz membership
        userGroupMembershipRepository.save(membership);
    }

    // Sprawdza, czy użytkownik należy do grupy rodzinnej
    public boolean userExistInFamily(String userEmail, long groupId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        // Sprawdź, czy istnieje membership między użytkownikiem a grupą
        Optional<UserGroupMembership> membership = userGroupMembershipRepository
                .findByUserAndFamilyGroup(user, familyGroup);

        return membership.isPresent();
    }

    // Pobiera wszystkie grupy rodzinne, do których należy użytkownik
    public List<FamilyGroup> getUserFamilies(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return familyGroupRepository.findGroupsByUserId(user.getId());
    }

    // Pobiera grupę rodzinną po ID lub rzuca wyjątek jeśli nie istnieje
    public FamilyGroup getFamilyGroupById(long groupId) {
        return familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));
    }
}
