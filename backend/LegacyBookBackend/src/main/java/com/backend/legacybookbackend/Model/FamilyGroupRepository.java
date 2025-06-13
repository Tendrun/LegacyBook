package com.backend.legacybookbackend.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Repozytorium do operacji na encji FamilyGroup
public interface FamilyGroupRepository extends JpaRepository<FamilyGroup, Long> {

    // Znajdź wszystkie grupy, do których należy użytkownik o danym ID
    @Query("SELECT m.familyGroup FROM UserGroupMembership m WHERE m.user.id = :userId")
    List<FamilyGroup> findGroupsByUserId(@Param("userId") Long userId);

    // Znajdź grupy po nazwie rodziny
    List<FamilyGroup> findByFamilyName(String FamilyGroupName);

    // Utwórz nową grupę rodzinną – zapytanie SQL
    @Modifying
    @Query(value = "INSERT INTO family_group (family_name) VALUES (:familyName)", nativeQuery = true)
    void createFamilyGroup(@Param("familyName") String familyName);

    // Usuń grupę rodzinną na podstawie jej ID – zapytanie SQL
    @Modifying
    @Query(value = "DELETE FROM family_group WHERE id = :id", nativeQuery = true)
    void deleteFamilyGroup(@Param("id") long id);

    // Dodaj użytkownika do grupy (zapisz relację do tabeli pośredniej user_group_link)
    @Modifying
    @Query(value = "INSERT INTO user_group_link (user_id, group_id) VALUES (:userId, :groupId)", nativeQuery = true)
    void addMemberToFamily(@Param("userId") long userId, @Param("groupId") long groupId);

    // Usuń użytkownika z grupy
    @Modifying
    @Query(value = "DELETE FROM user_group_link WHERE user_id = :userId AND group_id = :groupId", nativeQuery = true)
    void deleteMemberFromFamilyGroup(@Param("userId") long userId, @Param("groupId") long familyGroupId);
}
