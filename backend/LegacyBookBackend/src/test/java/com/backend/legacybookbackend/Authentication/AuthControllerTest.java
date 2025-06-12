package com.backend.legacybookbackend.Authentication;

import com.backend.legacybookbackend.Controller.AuthController;
import com.backend.legacybookbackend.DTO.AuthResponse;
import com.backend.legacybookbackend.DTO.LoginRequest;
import com.backend.legacybookbackend.DTO.RegisterRequest;
import com.backend.legacybookbackend.Services.AuthService;
import com.backend.legacybookbackend.Services.FamilyGroupService;
import com.backend.legacybookbackend.Services.UserGroupMembershipService;
import com.backend.legacybookbackend.Model.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private FamilyGroupService familyGroupService;

    @Mock
    private UserGroupMembershipService userGroupMembershipService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder; // Dodano zmockowany PasswordEncoder

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authService, familyGroupService, userGroupMembershipService, userRepository, passwordEncoder);
    }

    @Test
    void testRegister_Success() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(authService.register(request)).thenReturn("fake-jwt");

        // Act
        ResponseEntity<AuthResponse> response = authController.register(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("fake-jwt", response.getBody().getToken());
        verify(authService, times(1)).register(request);
    }

    @Test
    void testLogin_Success() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        AuthResponse authResponse = new AuthResponse("fake-jwt", "Test User");
        when(authService.login(request)).thenReturn(authResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("fake-jwt", response.getBody().getToken());
        assertEquals("Test User", response.getBody().getUsername());
        verify(authService, times(1)).login(request);
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(authService.register(request)).thenThrow(new RuntimeException("Email already registered"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authController.register(request));
        assertEquals("Email already registered", exception.getMessage());
        verify(authService, times(1)).register(request);
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrong-password");

        when(authService.login(request)).thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authController.login(request));
        assertEquals("Invalid credentials", exception.getMessage());
        verify(authService, times(1)).login(request);
    }
}