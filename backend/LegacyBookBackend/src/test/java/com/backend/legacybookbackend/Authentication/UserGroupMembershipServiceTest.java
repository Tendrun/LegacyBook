package com.backend.legacybookbackend.Authentication;

import com.backend.legacybookbackend.Model.User;
import com.backend.legacybookbackend.Model.FamilyGroupRepository;
import com.backend.legacybookbackend.Model.UserGroupMembershipRepository;
import com.backend.legacybookbackend.Model.UserRepository;
import com.backend.legacybookbackend.Services.UserGroupMembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserGroupMembershipServiceTest {

    private UserRepository userRepository;
    private UserGroupMembershipRepository userGroupMembershipRepository;
    private FamilyGroupRepository familyGroupRepository;
    private UserGroupMembershipService userGroupMembershipService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userGroupMembershipRepository = mock(UserGroupMembershipRepository.class);
        familyGroupRepository = mock(FamilyGroupRepository.class);
        userGroupMembershipService = new UserGroupMembershipService(
                familyGroupRepository, userRepository, userGroupMembershipRepository
        );
    }

    @Test
    void testHasHighLevelAccess_AdminRole() throws Exception {
        String userEmail = "test@example.com";
        long groupId = 1L;
        User user = new User();
        setId(user, 1L);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(userGroupMembershipRepository.getRole(groupId, user.getId())).thenReturn("Admin");

        boolean result = userGroupMembershipService.hasHighLevelAccess(userEmail, groupId);

        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(userGroupMembershipRepository, times(1)).getRole(groupId, user.getId());
    }

    @Test
    void testHasHighLevelAccess_NoAccess() throws Exception {
        String userEmail = "test@example.com";
        long groupId = 1L;
        User user = new User();
        setId(user, 1L);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(userGroupMembershipRepository.getRole(groupId, user.getId())).thenReturn("Member");

        boolean result = userGroupMembershipService.hasHighLevelAccess(userEmail, groupId);

        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(userGroupMembershipRepository, times(1)).getRole(groupId, user.getId());
    }

    @Test
    void testHasHighLevelAccess_UserNotFound() {
        String userEmail = "nonexistent@example.com";
        long groupId = 1L;

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        boolean result = userGroupMembershipService.hasHighLevelAccess(userEmail, groupId);

        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(userEmail);
        verifyNoInteractions(userGroupMembershipRepository);
    }

    private void setId(User user, Long id) throws Exception {
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, id);
    }
}