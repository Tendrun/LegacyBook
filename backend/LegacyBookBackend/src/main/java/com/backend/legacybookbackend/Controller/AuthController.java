package com.backend.legacybookbackend.Controller;

import com.backend.legacybookbackend.DTO.AuthResponse;
import com.backend.legacybookbackend.DTO.FamilyGroup.AddMemberRequest;
import com.backend.legacybookbackend.DTO.FamilyGroup.CreateGroupRequest;
import com.backend.legacybookbackend.DTO.FamilyGroup.DeleteMemberRequest;
import com.backend.legacybookbackend.DTO.LoginRequest;
import com.backend.legacybookbackend.Services.AuthService;
import com.backend.legacybookbackend.Services.FamilyGroupService;
import com.backend.legacybookbackend.DTO.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final FamilyGroupService familyGroupService;
    public AuthController(AuthService authService, FamilyGroupService familyGroupService) {
        this.authService = authService;
        this.familyGroupService = familyGroupService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, authenticated user!");
    }


    @PostMapping("/CreateFamilyGroup")
    public ResponseEntity<String> createFamilyGroup(@RequestBody CreateGroupRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        familyGroupService.createFamilyGroup(request, userEmail);
        return ResponseEntity.ok("Group created and user added");
    }

    @PostMapping("/AddMemberToFamilyGroup")
    public ResponseEntity<String> addMemberFamilyGroup(@RequestBody AddMemberRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if User is at least Owner or Admin
        boolean userIsAllowed = familyGroupService.isUserAllowedToAddMember(userEmail, request.getGroupId());

        if (!userIsAllowed) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Access denied: you are not allowed to add members to this group.");
        }

        // Check if user already exists as member in family

        // Add Member to Group
        else {
            familyGroupService.addMemberToFamily(request.getUserEmailToAdd(), request.getGroupId());
            return ResponseEntity.ok("Member added to group");
        }
    }

    @PostMapping("/DeleteMemberToFamilyGroup")
    public ResponseEntity<String> DeleteMemberToFamilyGroup(@RequestBody DeleteMemberRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if User is at least Owner or Admin
        boolean userIsAllowed = familyGroupService.isUserAllowedToAddMember(userEmail, request.getGroupId());

        if (!userIsAllowed) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Access denied: you are not allowed to add members to this group.");
        }

        // Delete Member to Group
        else {
            familyGroupService.deleteMemberToFamily(request.getUserEmailToDelete(), request.getGroupId());
            return ResponseEntity.ok("Member deleted to group");
        }
    }
}
