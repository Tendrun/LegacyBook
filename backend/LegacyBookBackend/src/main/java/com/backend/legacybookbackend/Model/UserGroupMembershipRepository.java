package com.backend.legacybookbackend.Model;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repozytorium JPA dla encji {@link UserGroupMembership}.
 * <p>
 * Zapewnia metody do zarządzania powiązaniami użytkowników z grupami rodzinnymi,
 * w tym zapytania niestandardowe dla ról i aktualizacji.
 */
public interface UserGroupMembershipRepository extends JpaRepository<UserGroupMembership, Long> {

    /**
     * Pobiera rolę użytkownika w konkretnej grupie rodzinnej.
     *
     * @param FamilyID identyfikator grupy rodzinnej
     * @param UserID identyfikator użytkownika
     * @return nazwa roli użytkownika w grupie (np. "Admin", "Owner", "User")
     */
    @Query(value = "SELECT role FROM user_group_link WHERE family_group_id = :FamilyID AND user_id = :UserID", nativeQuery = true)
    String getRole(@Param("FamilyID") long FamilyID, @Param("UserID") long UserID);

    /**
     * Aktualizuje rodziną rolę użytkownika w danej grupie.
     *
     * @param userId identyfikator użytkownika
     * @param groupId identyfikator grupy rodzinnej
     * @param familyRole nowa rodzinna rola użytkownika (np. "Father", "Mother", "None")
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_group_link SET family_role = :role WHERE user_id = :userId AND family_group_id = :groupId", nativeQuery = true)
    void updateFamilyRole(@Param("userId") long userId,
                          @Param("groupId") long groupId,
                          @Param("role") String familyRole);

    /**
     * Aktualizuje rolę użytkownika w grupie (np. "Admin", "Owner", "User").
     *
     * @param userId identyfikator użytkownika
     * @param groupId identyfikator grupy rodzinnej
     * @param role nowa rola użytkownika w grupie
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_group_link SET role = :role WHERE user_id = :userId AND family_group_id = :groupId", nativeQuery = true)
    void updateRole(@Param("userId") long userId,
                    @Param("groupId") long groupId,
                    @Param("role") String role);

    /**
     * Znajduje wszystkie powiązania członków z daną grupą rodzinną.
     *
     * @param group grupa rodzinna
     * @return lista powiązań użytkowników z podaną grupą
     */
    List<UserGroupMembership> findAllByFamilyGroup(FamilyGroup group);

    /**
     * Znajduje powiązanie użytkownika z grupą rodzinną.
     *
     * @param user użytkownik
     * @param familyGroup grupa rodzinna
     * @return obiekt Optional zawierający powiązanie, jeśli istnieje
     */
    Optional<UserGroupMembership> findByUserAndFamilyGroup(User user, FamilyGroup familyGroup);

}
