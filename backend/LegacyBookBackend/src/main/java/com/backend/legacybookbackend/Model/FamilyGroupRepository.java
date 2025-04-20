package com.backend.legacybookbackend.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface FamilyGroupRepository extends JpaRepository<FamilyGroup, Long> {
    @Query("SELECT m.familyGroup FROM UserGroupMembership m WHERE m.user.id = :userId")
    List<FamilyGroup> findGroupsByUserId(@Param("userId") Long userId);


    List<FamilyGroup> findByFamilyName(String FamilyGroupName);

    @Modifying
    @Query(value = "INSERT INTO family_group (family_name) VALUES (:familyName)", nativeQuery = true)
    void createFamilyGroup(String FamilyName);

    @Modifying
    @Query(value = "DELETE FROM family_group WHERE id = :id", nativeQuery = true)
    void deleteFamilyGroup(long id);

    @Modifying
    @Query(value = "INSERT INTO user_group_link (user_id, group_id) VALUES (:userId, :groupId)", nativeQuery = true)
    void addMemberToFamily(@Param("userId") long userId, @Param("groupId") long groupId);

    @Modifying
    @Query(value = "DELETE FROM user_group_link WHERE user_id = :userId AND group_id = :groupId", nativeQuery = true)
    void deleteMemberFromFamilyGroup(long User_id, long FamilyGroupId);

}
