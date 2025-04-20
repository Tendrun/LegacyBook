package com.backend.legacybookbackend.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserGroupMembershipRepository extends JpaRepository<UserGroupMembership, Long> {
    @Query(value = "SELECT role FROM user_group_link WHERE family_group_id = :FamilyID AND user_id = :UserID", nativeQuery = true)
    String getRole(@Param("FamilyID") long FamilyID,@Param("UserID") long UserID);

    Optional<UserGroupMembership> findByUserAndFamilyGroup(User user, FamilyGroup familyGroup);

}
