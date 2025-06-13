package com.backend.legacybookbackend.Model;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserGroupMembershipRepository extends JpaRepository<UserGroupMembership, Long> {

    // Pobiera rolę użytkownika w danej grupie rodzinnej (stringowa reprezentacja enuma Role)
    @Query(value = "SELECT role FROM user_group_link WHERE family_group_id = :FamilyID AND user_id = :UserID", nativeQuery = true)
    String getRole(@Param("FamilyID") long FamilyID, @Param("UserID") long UserID);

    // Aktualizuje rolę rodzinną (FamilyRole) użytkownika w konkretnej grupie rodzinnej
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_group_link SET family_role = :role WHERE user_id = :userId AND family_group_id = :groupId", nativeQuery = true)
    void updateFamilyRole(@Param("userId") long userId,
                          @Param("groupId") long groupId,
                          @Param("role") String familyRole);

    // Aktualizuje rolę (Role) użytkownika w konkretnej grupie rodzinnej
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_group_link SET role = :role WHERE user_id = :userId AND family_group_id = :groupId", nativeQuery = true)
    void updateRole(@Param("userId") long userId,
                    @Param("groupId") long groupId,
                    @Param("role") String role);

    // Znajduje wszystkie wpisy członkostwa dla danej grupy rodzinnej
    List<UserGroupMembership> findAllByFamilyGroup(FamilyGroup group);

    // Znajduje pojedyncze członkostwo po użytkowniku i grupie rodzinej (opcja, bo może nie istnieć)
    Optional<UserGroupMembership> findByUserAndFamilyGroup(User user, FamilyGroup familyGroup);

}
