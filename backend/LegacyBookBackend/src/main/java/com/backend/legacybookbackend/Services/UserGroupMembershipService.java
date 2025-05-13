package com.backend.legacybookbackend.Services;

import com.backend.legacybookbackend.Exception.FamilyGroupNotFoundException;
import com.backend.legacybookbackend.Exception.UserNotFoundException;
import com.backend.legacybookbackend.Model.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
@Service
public class UserGroupMembershipService {

    private final FamilyGroupRepository familyGroupRepository;
    private final UserRepository userRepository;
    private final UserGroupMembershipRepository userGroupMembershipRepository;


    public UserGroupMembershipService(FamilyGroupRepository familyGroupRepository, UserRepository userRepository, UserGroupMembershipRepository userGroupMembershipRepository){
        this.familyGroupRepository = familyGroupRepository;
        this.userRepository = userRepository;
        this.userGroupMembershipRepository = userGroupMembershipRepository;
    }

    public boolean hasHighLevelAccess(String userEmail, long GroupID){
        Optional<User> existingUser = userRepository.findByEmail(userEmail);

        try {
            User userEntity = existingUser.get();
            long UserID = userEntity.getId();
            String UserRole = userGroupMembershipRepository.getRole(GroupID, UserID);

            System.out.println("UserRole = " + UserRole);

            if(Objects.equals(UserRole, "Admin") || Objects.equals(UserRole, "Owner"))
                return true;

        } catch (NoSuchElementException e){
            return false;
        }

        return false;
    }


    public void setFamilyRole(String userEmail, long groupId, String familyRole ){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        UserGroupMembership membership = userGroupMembershipRepository
                .findByUserAndFamilyGroup(user, familyGroup)
                .orElseThrow(() -> new RuntimeException("MembershipNotFound"));

        long userId = user.getId();
        userGroupMembershipRepository.updateFamilyRole(userId,groupId,familyRole);
    }

    public void setRole(String userEmail, long groupId, String userRoleStr) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        UserGroupMembership membership = userGroupMembershipRepository
                .findByUserAndFamilyGroup(user, familyGroup)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        long userId = user.getId();

        // ✅ Walidacja i konwersja string → enum
        UserGroupMembership.Role roleEnum;
        try {
            roleEnum = UserGroupMembership.Role.valueOf(userRoleStr); // np. "Admin"
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + userRoleStr);
        }

        // ✅ Aktualizacja
        userGroupMembershipRepository.updateRole(userId, groupId, roleEnum.name());
    }

}
