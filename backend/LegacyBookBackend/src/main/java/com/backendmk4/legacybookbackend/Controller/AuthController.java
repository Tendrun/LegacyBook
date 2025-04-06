package com.backendmk4.legacybookbackend.Controller;

import com.backendmk4.legacybookbackend.DTO.FamilyGroupRequest;
import com.backendmk4.legacybookbackend.DTO.RegisterRequest;
import com.backendmk4.legacybookbackend.DTO.AuthResponse;
import com.backendmk4.legacybookbackend.DTO.LoginRequest;
import com.backendmk4.legacybookbackend.Services.AuthService;
import com.backendmk4.legacybookbackend.Services.FamilyGroupService;
import org.apache.coyote.Response;
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


    //TODO
    @PostMapping("/CreateFamilyGroup")
    public ResponseEntity<String> createFamilyGroup(@RequestBody FamilyGroupRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        familyGroupService.createFamilyGroup(request, userEmail);
        return ResponseEntity.ok("Group created and user added");
    }
}
