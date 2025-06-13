package com.backend.legacybookbackend.Services;

import com.backend.legacybookbackend.Exception.FamilyGroupNotFoundException;
import com.backend.legacybookbackend.Exception.UserNotFoundException;
import com.backend.legacybookbackend.Model.*;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserGroupMembershipService {

    private final FamilyGroupRepository familyGroupRepository;
    private final UserRepository userRepository;
    private final UserGroupMembershipRepository userGroupMembershipRepository;

    // Konstruktor do wstrzykiwania repozytoriów
    public UserGroupMembershipService(FamilyGroupRepository familyGroupRepository, UserRepository userRepository, UserGroupMembershipRepository userGroupMembershipRepository){
        this.familyGroupRepository = familyGroupRepository;
        this.userRepository = userRepository;
        this.userGroupMembershipRepository = userGroupMembershipRepository;
    }

    /**
     * Sprawdza, czy użytkownik o danym emailu ma wysoki poziom dostępu (Admin lub Owner) do konkretnej grupy.
     * @param userEmail email użytkownika
     * @param GroupID id grupy rodzinnej
     * @return true jeśli rola to Admin lub Owner, w przeciwnym wypadku false
     */
    public boolean hasHighLevelAccess(String userEmail, long GroupID){
        Optional<User> existingUser = userRepository.findByEmail(userEmail);

        try {
            User userEntity = existingUser.get();       // Pobieramy użytkownika lub rzucamy wyjątek
            long UserID = userEntity.getId();
            String UserRole = userGroupMembershipRepository.getRole(GroupID, UserID);  // Pobieramy rolę użytkownika w grupie

            System.out.println("UserRole = " + UserRole);

            if(Objects.equals(UserRole, "Admin") || Objects.equals(UserRole, "Owner"))
                return true;

        } catch (NoSuchElementException e){
            // Jeśli użytkownik nie istnieje lub brak roli, zwracamy false
            return false;
        }

        return false;
    }

    /**
     * Ustawia rolę rodzinną (FamilyRole) dla użytkownika w danej grupie
     * @param userEmail email użytkownika
     * @param groupId id grupy rodzinnej
     * @param familyRole nowa rola rodzinna do ustawienia (np. "Parent", "Child", itp.)
     */
    public void setFamilyRole(String userEmail, long groupId, String familyRole ){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));  // Znajdź użytkownika lub rzuc wyjątek

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found")); // Znajdź grupę lub rzuc wyjątek

        UserGroupMembership membership = userGroupMembershipRepository
                .findByUserAndFamilyGroup(user, familyGroup)
                .orElseThrow(() -> new RuntimeException("MembershipNotFound")); // Pobierz członkostwo, jeśli brak rzuca wyjątek

        long userId = user.getId();

        // Aktualizuj rolę rodzinną w repozytorium (bazie danych)
        userGroupMembershipRepository.updateFamilyRole(userId, groupId, familyRole);
    }

    /**
     * Ustawia podstawową rolę użytkownika (Role) w danej grupie
     * @param userEmail email użytkownika
     * @param groupId id grupy rodzinnej
     * @param userRoleStr nowa rola użytkownika do ustawienia jako String (np. "Admin", "User")
     */
    public void setRole(String userEmail, long groupId, String userRoleStr) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));  // Znajdź użytkownika lub rzuc wyjątek

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found")); // Znajdź grupę lub rzuc wyjątek

        UserGroupMembership membership = userGroupMembershipRepository
                .findByUserAndFamilyGroup(user, familyGroup)
                .orElseThrow(() -> new RuntimeException("Membership not found")); // Pobierz członkostwo, jeśli brak rzuca wyjątek

        long userId = user.getId();

        // Walidacja i konwersja String na enum Role
        UserGroupMembership.Role roleEnum;
        try {
            roleEnum = UserGroupMembership.Role.valueOf(userRoleStr); // np. "Admin"
        } catch (IllegalArgumentException e) {
            // Jeśli niepoprawna rola, rzucamy wyjątek z informacją
            throw new RuntimeException("Invalid role: " + userRoleStr);
        }

        // Aktualizujemy rolę w repozytorium (bazie danych)
        userGroupMembershipRepository.updateRole(userId, groupId, roleEnum.name());
    }

}
