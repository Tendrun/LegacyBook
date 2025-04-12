package com.backendmk4.legacybookbackend.Services;

import com.backendmk4.legacybookbackend.DTO.FamilyGroup.CreateGroupRequest;
import com.backendmk4.legacybookbackend.Model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backendmk4.legacybookbackend.Model.UserGroupMembership.Role;



import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

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

    public boolean isUserAllowedToAddMember(String userEmail, long GroupID){
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

    public void addMemberToFamily(String userEmail, long groupId) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not Found"));
        FamilyGroup familyGroup = familyGroupRepository.findById(groupId).orElseThrow(
                () -> new RuntimeException("Family group not Found"));

        UserGroupMembership membership = new UserGroupMembership();

        membership.setUser(user);
        membership.setFamilyGroup(familyGroup);
        membership.setRole(UserGroupMembership.Role.User);
        membership.setFamilyRole(UserGroupMembership.FamilyRole.None);

        userGroupMembershipRepository.save(membership);
    }

    @Transactional
    public void createFamilyGroup(CreateGroupRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
}
