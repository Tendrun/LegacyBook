package com.backend.legacybookbackend.Authentication;

import com.backend.legacybookbackend.Exception.FamilyGroupNotFoundException;
import com.backend.legacybookbackend.Exception.UserNotFoundException;
import com.backend.legacybookbackend.Model.*;
import com.backend.legacybookbackend.DTO.FamilyGroup.CreateGroupRequest;
import com.backend.legacybookbackend.Services.FamilyGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FamilyGroupServiceTest {

    private FamilyGroupRepository familyGroupRepository;
    private UserRepository userRepository;
    private UserGroupMembershipRepository userGroupMembershipRepository;
    private FamilyGroupService familyGroupService;

    @BeforeEach
    void setUp() {
        familyGroupRepository = mock(FamilyGroupRepository.class);
        userRepository = mock(UserRepository.class);
        userGroupMembershipRepository = mock(UserGroupMembershipRepository.class);
        familyGroupService = new FamilyGroupService(familyGroupRepository, userRepository, userGroupMembershipRepository);
    }

    @Test
    void testHasHighLevelAccess_AdminRole() {
        String userEmail = "test@example.com";
        long groupId = 1L;

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(userGroupMembershipRepository.getRole(groupId, user.getId())).thenReturn("Admin");

        boolean result = familyGroupService.hasHighLevelAccess(userEmail, groupId);

        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(userGroupMembershipRepository, times(1)).getRole(groupId, user.getId());
    }

    @Test
    void testAddMemberToFamily_UserNotFound() {
        String userEmail = "nonexistent@example.com";
        long groupId = 1L;

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> familyGroupService.addMemberToFamily(userEmail, groupId));
        verify(userRepository, times(1)).findByEmail(userEmail);
        verifyNoInteractions(familyGroupRepository);
    }

    @Test
    void testDeleteFamily_GroupNotFound() {
        long groupId = 1L;

        when(familyGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> familyGroupService.deleteFamily(groupId));
        verify(familyGroupRepository, times(1)).findById(groupId);
    }

    @Test
    void testDeleteFamily_Success() {
        long groupId = 1L;
        FamilyGroup group = new FamilyGroup();
        group.setFamilyName("Test Family");

        when(familyGroupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userGroupMembershipRepository.findAllByFamilyGroup(group)).thenReturn(List.of());

        familyGroupService.deleteFamily(groupId);

        verify(userGroupMembershipRepository, times(1)).findAllByFamilyGroup(group);
        verify(userGroupMembershipRepository, times(1)).deleteAll(anyList());
        verify(familyGroupRepository, times(1)).delete(group);
    }
}