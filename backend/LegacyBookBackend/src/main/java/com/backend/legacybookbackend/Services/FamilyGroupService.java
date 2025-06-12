package com.backend.legacybookbackend.Services;

import com.backend.legacybookbackend.DTO.FamilyGroup.FamilyGroupDTO;
import com.backend.legacybookbackend.DTO.FamilyGroup.MemberDTO;
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
import java.util.stream.Collectors;

@Service
public class FamilyGroupService {

    private final FamilyGroupRepository familyGroupRepository;
    private final UserRepository userRepository;
    private final UserGroupMembershipRepository userGroupMembershipRepository;


    public FamilyGroupService(FamilyGroupRepository familyGroupRepository, UserRepository userRepository, UserGroupMembershipRepository userGroupMembershipRepository){
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

    public boolean UserIsFamilyOwner(String userEmail, long GroupID){
        Optional<User> existingUser = userRepository.findByEmail(userEmail);

        try {
            User userEntity = existingUser.get();
            long UserID = userEntity.getId();
            String UserRole = userGroupMembershipRepository.getRole(GroupID, UserID);

            System.out.println("UserRole = " + UserRole);

            if(Objects.equals(UserRole, "Owner"))
                return true;

        } catch (NoSuchElementException e){
            return false;
        }

        return false;
    }
    @Transactional
    public void deleteFamily(long groupID){
        FamilyGroup group = familyGroupRepository.findById(groupID)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Usuń wszystkie memberships ręcznie
        List<UserGroupMembership> memberships = userGroupMembershipRepository.findAllByFamilyGroup(group);
        userGroupMembershipRepository.deleteAll(memberships);

        // Teraz możesz bezpiecznie usunąć grupę
        familyGroupRepository.delete(group);
    }

    public void addMemberToFamily(String userEmail, long groupId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        UserGroupMembership membership = new UserGroupMembership();

        membership.setUser(user);
        membership.setFamilyGroup(familyGroup);
        membership.setRole(UserGroupMembership.Role.User);
        membership.setFamilyRole(UserGroupMembership.FamilyRole.None);

        userGroupMembershipRepository.save(membership);
    }

    public void deleteMemberToFamily(String userEmail, long groupId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        UserGroupMembership membership = userGroupMembershipRepository
                .findByUserAndFamilyGroup(user, familyGroup)
                .orElseThrow(() -> new RuntimeException("MembershipNotFound"));

        userGroupMembershipRepository.delete(membership);
    }

    @Transactional
    public void createFamilyGroup(CreateGroupRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup group = new FamilyGroup();                // Create new group
        group.setFamilyName(request.getFamilyName());         // Set the name for group

        familyGroupRepository.save(group);

        // Create membership and assign role
        UserGroupMembership membership = new UserGroupMembership();
        membership.setUser(user);
        membership.setFamilyGroup(group);
        membership.setFamilyRole(UserGroupMembership.FamilyRole.None); // or dynamically set from request if needed
        membership.setRole(UserGroupMembership.Role.Owner); // or dynamically set from request if needed


        userGroupMembershipRepository.save(membership);
    }

    public boolean userExistInFamily(String userEmail, long groupId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        Optional<UserGroupMembership> membership = userGroupMembershipRepository
                .findByUserAndFamilyGroup(user, familyGroup);

        return membership.isPresent();
    }

    public List<FamilyGroup> getUserFamilies(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return familyGroupRepository.findGroupsByUserId(user.getId());
    }
    public FamilyGroupDTO getFamilyGroupById(long groupId) {
        List<MemberDTO> members = List.of(
                new MemberDTO("Owner", "Father", "Jan Kowalski", "jan@example.com"),
                new MemberDTO("User", "Brother", "Piotr Nowak", "piotr@example.com")
        );

        return new FamilyGroupDTO(groupId, "Testowa Rodzina", members);
    }

}
