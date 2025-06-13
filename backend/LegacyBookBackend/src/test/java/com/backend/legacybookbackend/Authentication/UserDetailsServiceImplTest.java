package com.backend.legacybookbackend.Authentication;

import com.backend.legacybookbackend.Model.User;
import com.backend.legacybookbackend.Model.UserRepository;
import com.backend.legacybookbackend.Services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        // Mock użytkownika
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password123");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Wywołanie metody
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Weryfikacja
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Mock brakującego użytkownika
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Wywołanie metody i oczekiwanie wyjątku
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));

        // Weryfikacja
        verify(userRepository, times(1)).findByEmail(email);
    }
}