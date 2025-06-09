package com.backend.legacybookbackend.Controller;

import com.backend.legacybookbackend.DTO.AuthResponse;
import com.backend.legacybookbackend.DTO.FamilyGroup.*;
import com.backend.legacybookbackend.DTO.LoginRequest;
import com.backend.legacybookbackend.Model.FamilyGroup;
import com.backend.legacybookbackend.Model.UserRepository;
import com.backend.legacybookbackend.Services.AuthService;
import com.backend.legacybookbackend.Services.FamilyGroupService;
import com.backend.legacybookbackend.DTO.RegisterRequest;
import com.backend.legacybookbackend.Services.UserGroupMembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.backend.legacybookbackend.Model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final FamilyGroupService familyGroupService;
    private final UserGroupMembershipService userGroupMembershipService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, FamilyGroupService familyGroupService, UserGroupMembershipService userGroupMembershipService, UserRepository userRepository) {
        this.authService = authService;
        this.familyGroupService = familyGroupService;
        this.userGroupMembershipService = userGroupMembershipService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        String token = authService.register(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
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
    @GetMapping("/GetUserFamilies")
    public ResponseEntity<List<FamilyGroup>> getUserFamilies() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<FamilyGroup> families = familyGroupService.getUserFamilies(userEmail);
        return ResponseEntity.ok(families);
    }

    @GetMapping("/GetFamilyGroupDetails")
    public ResponseEntity<FamilyGroup> getFamilyGroupDetails(@RequestParam long groupId) {
        FamilyGroup familyGroup = familyGroupService.getFamilyGroupById(groupId);
        return ResponseEntity.ok(familyGroup);
    }

    @PostMapping(value = "/updateProfilePicture", consumes = "multipart/form-data")
    public ResponseEntity<String> updateProfilePicture(
            @RequestPart("profilePicture") MultipartFile profilePicture,
            @RequestHeader("Authorization") String token) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            // Zapisz plik na serwerze lub w chmurze
            String filePath = saveProfilePicture(profilePicture);

            // Zaktualizuj ścieżkę w bazie danych
            user.setProfilePicture(filePath);
            userRepository.save(user);

            return ResponseEntity.ok("Profile picture updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture");
        }
    }
    private String saveProfilePicture(MultipartFile file) throws IOException {
        // Przykładowa ścieżka zapisu pliku
        String uploadDir = "uploads/profile_pictures/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return filePath.toString();
    }
}
