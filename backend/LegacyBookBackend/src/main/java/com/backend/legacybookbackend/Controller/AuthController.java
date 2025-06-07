package com.backend.legacybookbackend.Controller;

import com.backend.legacybookbackend.DTO.AuthResponse;
import com.backend.legacybookbackend.DTO.FamilyGroup.*;
import com.backend.legacybookbackend.DTO.LoginRequest;
import com.backend.legacybookbackend.Services.AuthService;
import com.backend.legacybookbackend.Services.FamilyGroupService;
import com.backend.legacybookbackend.DTO.RegisterRequest;
import com.backend.legacybookbackend.Services.UserGroupMembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final FamilyGroupService familyGroupService;
    private final UserGroupMembershipService userGroupMembershipService;

    public AuthController(AuthService authService, FamilyGroupService familyGroupService, UserGroupMembershipService userGroupMembershipService) {
        this.authService = authService;
        this.familyGroupService = familyGroupService;
        this.userGroupMembershipService = userGroupMembershipService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        String token = authService.register(request);
        return ResponseEntity.ok(new AuthResponse(token));
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


        // Check if user already exists as member in family
        boolean userAlreadyExists = familyGroupService.userExistInFamily(request.getUserEmailToAdd(), request.getGroupId());

        if(userAlreadyExists)
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("User already exist in this group");

        // Check if User is at least Owner or Admin
        boolean userIsAllowed = familyGroupService.hasHighLevelAccess(userEmail, request.getGroupId());

        if (!userIsAllowed) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Access denied: you are not allowed to add members to this group.");
        }
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
        boolean userIsAllowed = familyGroupService.hasHighLevelAccess(userEmail, request.getGroupId());

        // Check if user exists as member in family
        boolean userAlreadyExists = familyGroupService.userExistInFamily(request.getUserEmailToDelete(), request.getGroupId());

        if(!userAlreadyExists)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User doesn't exist in this group");

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

    @PostMapping("/DeleteFamily")
    public ResponseEntity<String> DeleteFamily(@RequestBody DeleteFamilyRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if User is at least Owner or Admin
        boolean userIsAllowed = familyGroupService.UserIsFamilyOwner(userEmail, request.getGroupId());


        if (!userIsAllowed) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Access denied: you are not allowed to delete Family in this group!!");
        }

        // Delete Member to Group
        else {
            familyGroupService.deleteFamily(request.getGroupId());
            return ResponseEntity.ok("Family deleted successfully!!!");
        }
    }

    @PostMapping("/SetFamilyRole")
    public ResponseEntity<String> setFamilyRole(@RequestBody SetFamilyRoleRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if User is at least Owner
        boolean userIsAllowed = familyGroupService.UserIsFamilyOwner(userEmail, request.getGroupId());
        System.out.println("request.getUserEmailRole() " + request.getUserEmailRole());

        boolean userAlreadyExists = familyGroupService.userExistInFamily(request.getUserEmailRole(), request.getGroupId());

        if(!userAlreadyExists)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User doesn't exist in this group");

        if (!userIsAllowed) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Access denied: you are not allowed to set Family Role in this group!!");
        }

        else {
            userGroupMembershipService.setFamilyRole(request.getUserEmailRole(), request.getGroupId(), request.getFamilyRole());
            return ResponseEntity.ok("You set family role successfully!!!");
        }
    }

    @PostMapping("/SetRole")
    public ResponseEntity<String> setFamilyRole(@RequestBody SetRoleRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if User is at least Owner
        boolean userIsAllowed = familyGroupService.hasHighLevelAccess(userEmail, request.getGroupId());
        System.out.println("request.getUserEmailRole() " + request.getUserEmailRole());

        boolean userAlreadyExists = familyGroupService.userExistInFamily(request.getUserEmailRole(), request.getGroupId());

        if(!userAlreadyExists)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User doesn't exist in this group");

        if (!userIsAllowed) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Access denied: you are not allowed to set Role in this group!!");
        }

        else {
            System.out.println(request.getRole());
            userGroupMembershipService.setRole(request.getUserEmailRole(), request.getGroupId(), request.getRole());
            return ResponseEntity.ok("You set role successfully!!!");
        }
    }
}
