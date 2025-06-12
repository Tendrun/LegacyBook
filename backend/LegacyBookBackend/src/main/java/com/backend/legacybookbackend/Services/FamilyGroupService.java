package com.backend.legacybookbackend.Services;

import com.backend.legacybookbackend.DTO.FamilyGroup.FamilyGroupDTO;
import com.backend.legacybookbackend.DTO.FamilyGroup.MemberDTO;
import com.backend.legacybookbackend.Exception.FamilyGroupNotFoundException;
import com.backend.legacybookbackend.Exception.UserNotFoundException;
import com.backend.legacybookbackend.Model.*;
import com.backend.legacybookbackend.DTO.FamilyGroup.CreateGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serwis odpowiedzialny za obsługę logiki biznesowej związanej z rodzinami i przynależnością użytkowników do grup rodzinnych.
 */
@Service
public class FamilyGroupService {

    private final FamilyGroupRepository familyGroupRepository;
    private final UserRepository userRepository;
    private final UserGroupMembershipRepository userGroupMembershipRepository;

    /**
     * Konstruktor klasy FamilyGroupService.
     *
     * @param familyGroupRepository repozytorium grup rodzinnych
     * @param userRepository repozytorium użytkowników
     * @param userGroupMembershipRepository repozytorium przynależności użytkowników do grup
     */
    public FamilyGroupService(FamilyGroupRepository familyGroupRepository, UserRepository userRepository, UserGroupMembershipRepository userGroupMembershipRepository){
        this.familyGroupRepository = familyGroupRepository;
        this.userRepository = userRepository;
        this.userGroupMembershipRepository = userGroupMembershipRepository;
    }

    /**
     * Sprawdza, czy użytkownik ma dostęp wyższego poziomu (Admin lub Owner) w danej grupie rodzinnej.
     *
     * @param userEmail adres e-mail użytkownika
     * @param groupId identyfikator grupy
     * @return true jeśli użytkownik ma dostęp Admin lub Owner, false w przeciwnym wypadku
     */
    public boolean hasHighLevelAccess(String userEmail, long groupId){
        Optional<User> existingUser = userRepository.findByEmail(userEmail);
        try {
            User userEntity = existingUser.get();
            long userId = userEntity.getId();
            String userRole = userGroupMembershipRepository.getRole(groupId, userId);
            return Objects.equals(userRole, "Admin") || Objects.equals(userRole, "Owner");
        } catch (NoSuchElementException e){
            return false;
        }
    }

    /**
     * Sprawdza, czy użytkownik jest właścicielem danej grupy rodzinnej.
     *
     * @param userEmail adres e-mail użytkownika
     * @param groupId identyfikator grupy
     * @return true jeśli użytkownik jest właścicielem grupy, false w przeciwnym wypadku
     */
    public boolean UserIsFamilyOwner(String userEmail, long groupId){
        Optional<User> existingUser = userRepository.findByEmail(userEmail);
        try {
            User userEntity = existingUser.get();
            long userId = userEntity.getId();
            String userRole = userGroupMembershipRepository.getRole(groupId, userId);
            return Objects.equals(userRole, "Owner");
        } catch (NoSuchElementException e){
            return false;
        }
    }

    /**
     * Usuwa grupę rodzinną oraz powiązane członkostwa użytkowników.
     *
     * @param groupId identyfikator grupy do usunięcia
     */
    @Transactional
    public void deleteFamily(long groupId){
        FamilyGroup group = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<UserGroupMembership> memberships = userGroupMembershipRepository.findAllByFamilyGroup(group);
        userGroupMembershipRepository.deleteAll(memberships);
        familyGroupRepository.delete(group);
    }

    /**
     * Dodaje użytkownika jako członka do istniejącej grupy rodzinnej.
     *
     * @param userEmail adres e-mail użytkownika
     * @param groupId identyfikator grupy rodzinnej
     */
    public void addMemberToFamily(String userEmail, long groupId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        UserGroupMembership membership = new UserGroupMembership();
        membership.setUser(user);
        membership.setFamilyGroup(familyGroup);
        membership.setRole(UserGroupMembership.Role.User);
        membership.setFamilyRole(UserGroupMembership.FamilyRole.None);

        userGroupMembershipRepository.save(membership);
    }

    /**
     * Usuwa członka z grupy rodzinnej.
     *
     * @param userEmail adres e-mail użytkownika
     * @param groupId identyfikator grupy rodzinnej
     */
    public void deleteMemberToFamily(String userEmail, long groupId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        UserGroupMembership membership = userGroupMembershipRepository
                .findByUserAndFamilyGroup(user, familyGroup)
                .orElseThrow(() -> new RuntimeException("MembershipNotFound"));

        userGroupMembershipRepository.delete(membership);
    }

    /**
     * Tworzy nową grupę rodzinną i przypisuje użytkownikowi rolę właściciela.
     *
     * @param request dane wejściowe potrzebne do utworzenia grupy
     * @param userEmail adres e-mail użytkownika tworzącego grupę
     */
    @Transactional
    public void createFamilyGroup(CreateGroupRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup group = new FamilyGroup();
        group.setFamilyName(request.getFamilyName());

        familyGroupRepository.save(group);

        UserGroupMembership membership = new UserGroupMembership();
        membership.setUser(user);
        membership.setFamilyGroup(group);
        membership.setFamilyRole(UserGroupMembership.FamilyRole.None);
        membership.setRole(UserGroupMembership.Role.Owner);

        userGroupMembershipRepository.save(membership);
    }

    /**
     * Sprawdza, czy użytkownik należy do określonej grupy rodzinnej.
     *
     * @param userEmail adres e-mail użytkownika
     * @param groupId identyfikator grupy rodzinnej
     * @return true jeśli użytkownik należy do grupy, false w przeciwnym razie
     */
    public boolean userExistInFamily(String userEmail, long groupId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FamilyGroup familyGroup = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        Optional<UserGroupMembership> membership = userGroupMembershipRepository
                .findByUserAndFamilyGroup(user, familyGroup);

        return membership.isPresent();
    }

    /**
     * Pobiera listę grup rodzinnych, do których należy dany użytkownik.
     *
     * @param userEmail adres e-mail użytkownika
     * @return lista grup rodzinnych
     */
    public List<FamilyGroup> getUserFamilies(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return familyGroupRepository.findGroupsByUserId(user.getId());
    }

    /**
     * Pobiera szczegóły grupy rodzinnej na podstawie jej identyfikatora.
     *
     * @param groupId identyfikator grupy rodzinnej
     * @return DTO zawierający dane grupy i jej członków
     */
    public FamilyGroupDTO getFamilyGroupById(long groupId) {
        FamilyGroup group = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new FamilyGroupNotFoundException("Family group not found"));

        List<MemberDTO> memberDTOs = group.getMemberships().stream()
                .map(m -> new MemberDTO(
                        m.role.name(),
                        m.familyRole != null ? m.familyRole.name() : "None",
                        m.getUserName(),
                        m.getUserEmail()
                ))
                .collect(Collectors.toList());

        return new FamilyGroupDTO(group.getId(), group.getFamilyName(), memberDTOs);
    }
}
