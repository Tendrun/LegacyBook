package com.backendmk4.legacybookbackend.Controller;

import com.backendmk4.legacybookbackend.DTO.FamilyGroup.AddMemberRequest;
import com.backendmk4.legacybookbackend.DTO.FamilyGroup.CreateGroupRequest;
import com.backendmk4.legacybookbackend.DTO.RegisterRequest;
import com.backendmk4.legacybookbackend.DTO.AuthResponse;
import com.backendmk4.legacybookbackend.DTO.LoginRequest;
import com.backendmk4.legacybookbackend.Model.UserGroupMembership;
import com.backendmk4.legacybookbackend.Services.AuthService;
import com.backendmk4.legacybookbackend.Services.FamilyGroupService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.Console;

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

        // Add Member to Group
        else {
            familyGroupService.addMemberToFamily(userEmail, request.getGroupId());
            return ResponseEntity.ok("Member added to group");
        }
    }
}
