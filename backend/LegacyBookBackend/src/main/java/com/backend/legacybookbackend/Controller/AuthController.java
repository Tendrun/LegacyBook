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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.backend.legacybookbackend.Model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Kontroler odpowiedzialny za uwierzytelnianie użytkowników
 * oraz zarządzanie grupami rodzinnymi i ich członkami.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final FamilyGroupService familyGroupService;
    private final UserGroupMembershipService userGroupMembershipService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService,
                          FamilyGroupService familyGroupService,
                          UserGroupMembershipService userGroupMembershipService,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.familyGroupService = familyGroupService;
        this.userGroupMembershipService = userGroupMembershipService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Rejestruje nowego użytkownika i zwraca token JWT.
     *
     * @param request dane rejestracyjne użytkownika
     * @return token JWT w obiekcie AuthResponse
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        String token = authService.register(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Loguje użytkownika na podstawie email i hasła.
     *
     * @param request dane logowania użytkownika
     * @return token JWT oraz nazwa użytkownika
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Testowy endpoint zwracający powitanie dla uwierzytelnionego użytkownika.
     *
     * @return String powitalny
     */
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, authenticated user!");
    }

    /**
     * Tworzy nową grupę rodzinną i dodaje do niej użytkownika wywołującego.
     *
     * @param request dane nowej grupy rodzinnej
     * @return komunikat potwierdzający utworzenie grupy
     */
    @PostMapping("/CreateFamilyGroup")
    public ResponseEntity<String> createFamilyGroup(@RequestBody CreateGroupRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        familyGroupService.createFamilyGroup(request, userEmail);
        return ResponseEntity.ok("Group created and user added");
    }

    /**
     * Dodaje członka do grupy rodzinnej, jeżeli użytkownik ma odpowiednie uprawnienia.
     *
     * @param request dane dodawanego członka i grupy
     * @return odpowiedź HTTP z informacją o powodzeniu lub błędzie
     */
    @PostMapping("/AddMemberToFamilyGroup")
    public ResponseEntity<String> addMemberFamilyGroup(@RequestBody AddMemberRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean userAlreadyExists = familyGroupService.userExistInFamily(request.getUserEmailToAdd(), request.getGroupId());

        if (userAlreadyExists)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exist in this group");

        boolean userIsAllowed = familyGroupService.hasHighLevelAccess(userEmail, request.getGroupId());

        if (!userIsAllowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: you are not allowed to add members to this group.");
        } else {
            familyGroupService.addMemberToFamily(request.getUserEmailToAdd(), request.getGroupId());
            return ResponseEntity.ok("Member added to group");
        }
    }

    /**
     * Usuwa członka z grupy rodzinnej, jeżeli użytkownik ma odpowiednie uprawnienia.
     *
     * @param request dane usuwanego członka i grupy
     * @return odpowiedź HTTP z informacją o powodzeniu lub błędzie
     */
    @PostMapping("/DeleteMemberToFamilyGroup")
    public ResponseEntity<String> DeleteMemberToFamilyGroup(@RequestBody DeleteMemberRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("REQUEST: " + request);

        boolean userIsAllowed = familyGroupService.hasHighLevelAccess(userEmail, request.getGroupId());
        boolean userAlreadyExists = familyGroupService.userExistInFamily(request.getUserEmailToDelete(), request.getGroupId());

        if (!userAlreadyExists)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't exist in this group");

        if (!userIsAllowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: you are not allowed to add members to this group.");
        } else {
            familyGroupService.deleteMemberToFamily(request.getUserEmailToDelete(), request.getGroupId());
            return ResponseEntity.ok("Member deleted to group");
        }
    }

    /**
     * Usuwa całą grupę rodzinną, jeśli użytkownik jest właścicielem grupy.
     *
     * @param request dane grupy do usunięcia
     * @return odpowiedź HTTP z informacją o powodzeniu lub błędzie
     */
    @PostMapping("/DeleteFamily")
    public ResponseEntity<String> DeleteFamily(@RequestBody DeleteFamilyRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean userIsAllowed = familyGroupService.UserIsFamilyOwner(userEmail, request.getGroupId());

        if (!userIsAllowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: you are not allowed to delete Family in this group!!");
        } else {
            familyGroupService.deleteFamily(request.getGroupId());
            return ResponseEntity.ok("Family deleted successfully!!!");
        }
    }

    /**
     * Ustawia rolę rodzinną użytkownika, jeśli aktualny użytkownik jest właścicielem rodziny.
     *
     * @param request dane z emailem użytkownika, grupą i nową rolą
     * @return odpowiedź HTTP z informacją o powodzeniu lub błędzie
     */
    @PostMapping("/SetFamilyRole")
    public ResponseEntity<String> setFamilyRole(@RequestBody SetFamilyRoleRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean userIsAllowed = familyGroupService.UserIsFamilyOwner(userEmail, request.getGroupId());
        System.out.println("request.getUserEmailRole() " + request.getUserEmailRole());

        boolean userAlreadyExists = familyGroupService.userExistInFamily(request.getUserEmailRole(), request.getGroupId());

        if (!userAlreadyExists)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't exist in this group");

        if (!userIsAllowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: you are not allowed to set Family Role in this group!!");
        } else {
            userGroupMembershipService.setFamilyRole(request.getUserEmailRole(), request.getGroupId(), request.getFamilyRole());
            return ResponseEntity.ok("You set family role successfully!!!");
        }
    }

    /**
     * Ustawia rolę użytkownika w grupie (np. admin, member), jeśli użytkownik ma odpowiednie uprawnienia.
     *
     * @param request dane z emailem użytkownika, grupą i nową rolą
     * @return odpowiedź HTTP z informacją o powodzeniu lub błędzie
     */
    @PostMapping("/SetRole")
    public ResponseEntity<String> setFamilyRole(@RequestBody SetRoleRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean userIsAllowed = familyGroupService.hasHighLevelAccess(userEmail, request.getGroupId());
        System.out.println("request.getUserEmailRole() " + request.getUserEmailRole());

        boolean userAlreadyExists = familyGroupService.userExistInFamily(request.getUserEmailRole(), request.getGroupId());

        if (!userAlreadyExists)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't exist in this group");

        if (!userIsAllowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: you are not allowed to set Role in this group!!");
        } else {
            System.out.println(request.getRole());
            userGroupMembershipService.setRole(request.getUserEmailRole(), request.getGroupId(), request.getRole());
            return ResponseEntity.ok("You set role successfully!!!");
        }
    }

    /**
     * Pobiera listę rodzin, do których należy zalogowany użytkownik.
     *
     * @return lista grup rodzinnych użytkownika
     */
    @GetMapping("/GetUserFamilies")
    public ResponseEntity<List<FamilyGroup>> getUserFamilies() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<FamilyGroup> families = familyGroupService.getUserFamilies(userEmail);
        return ResponseEntity.ok(families);
    }

    /**
     * Pobiera szczegóły grupy rodzinnej po jej ID.
     *
     * @param groupId identyfikator grupy rodzinnej
     * @return obiekt DTO grupy rodzinnej z szczegółami
     */
    @GetMapping("/GetFamilyGroupDetails")
    public ResponseEntity<FamilyGroupDTO> getFamilyGroupDetails(@RequestParam long groupId) {
        FamilyGroupDTO familyGroup = familyGroupService.getFamilyGroupById(groupId);
        return ResponseEntity.ok(familyGroup);
    }

    /**
     * Prosty testowy endpoint zwracający wartość 1.
     *
     * @return liczba całkowita 1
     */
    @GetMapping("/GetTest")
    public int test() {
        return 1;
    }

    /**
     * Aktualizuje zdjęcie profilowe użytkownika.
     *
     * @param profilePicture plik zdjęcia profilowego
     * @param token nagłówek autoryzacji (JWT)
     * @return odpowiedź HTTP z informacją o powodzeniu lub błędzie
     * @throws IOException w przypadku błędu podczas zapisu pliku
     */
    @PostMapping(value = "/updateProfilePicture", consumes = "multipart/form-data")
    public ResponseEntity<String> updateProfilePicture(
            @RequestPart("profilePicture") MultipartFile profilePicture,
            @RequestHeader("Authorization") String token) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            String filePath = saveProfilePicture(profilePicture);
            user.setProfilePicture(filePath);
            userRepository.save(user);
            return ResponseEntity.ok("Profile picture updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture");
        }
    }

    /**
     * Zapisuje plik zdjęcia profilowego na serwerze.
     *
     * @param file plik do zapisania
     * @return ścieżka publicznego URL do pliku
     * @throws IOException w przypadku błędu podczas zapisu pliku
     */
    private String saveProfilePicture(MultipartFile file) throws IOException {
        String uploadDir = "uploads/profile_pictures/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return "/profile_pictures/" + fileName;
    }

    /**
     * Pobiera profil zalogowanego użytkownika, wraz z pełnym URL do zdjęcia profilowego.
     *
     * @return DTO profilu użytkownika
     */
    @GetMapping("/GetUserProfile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String baseUrl = "http://10.0.2.2:8080";
        String profilePictureUrl = user.getProfilePicture() != null ? baseUrl + user.getProfilePicture() : null;

        UserProfileDTO userProfileDTO = new UserProfileDTO(user.getName(), user.getEmail(), profilePictureUrl);
        return ResponseEntity.ok(userProfileDTO);
    }

    /**
     * Resetuje hasło użytkownika na nowe, po podaniu emaila i nowego hasła.
     *
     * @param request mapa z kluczami "email" oraz "newPassword"
     * @return odpowiedź HTTP z informacją o powodzeniu
     */
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully");
    }
}
