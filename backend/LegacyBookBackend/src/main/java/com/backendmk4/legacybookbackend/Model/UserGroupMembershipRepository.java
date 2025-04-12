package com.backendmk4.legacybookbackend.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserGroupMembershipRepository extends JpaRepository<UserGroupMembership, Long> {
    @Query(value = "SELECT role FROM user_group_link WHERE family_group_id = :FamilyID AND user_id = :UserID", nativeQuery = true)
    String getRole(@Param("FamilyID") long FamilyID,@Param("UserID") long UserID);
}
