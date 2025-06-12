package com.backend.legacybookbackend.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repozytorium JPA dla encji FamilyGroup.
 * Umożliwia wykonywanie operacji CRUD oraz niestandardowych zapytań związanych z grupami rodzinnymi.
 */
public interface FamilyGroupRepository extends JpaRepository<FamilyGroup, Long> {

    /**
     * Pobiera listę grup rodzinnych, do których należy użytkownik o podanym ID.
     *
     * @param userId ID użytkownika
     * @return lista grup rodzinnych użytkownika
     */
    @Query("SELECT m.familyGroup FROM UserGroupMembership m WHERE m.user.id = :userId")
    List<FamilyGroup> findGroupsByUserId(@Param("userId") Long userId);

    /**
     * Znajduje grupy rodzinne o podanej nazwie.
     *
     * @param FamilyGroupName nazwa grupy rodzinnej
     * @return lista grup o podanej nazwie
     */
    List<FamilyGroup> findByFamilyName(String FamilyGroupName);

    /**
     * Tworzy nową grupę rodzinną o podanej nazwie.
     *
     * @param FamilyName nazwa nowej grupy rodzinnej
     */
    @Modifying
    @Query(value = "INSERT INTO family_group (family_name) VALUES (:familyName)", nativeQuery = true)
    void createFamilyGroup(String FamilyName);

    /**
     * Usuwa grupę rodzinną o podanym ID.
     *
     * @param id ID grupy rodzinnej do usunięcia
     */
    @Modifying
    @Query(value = "DELETE FROM family_group WHERE id = :id", nativeQuery = true)
    void deleteFamilyGroup(long id);

    /**
     * Dodaje użytkownika do grupy rodzinnej.
     *
     * @param userId ID użytkownika do dodania
     * @param groupId ID grupy rodzinnej
     */
    @Modifying
    @Query(value = "INSERT INTO user_group_link (user_id, group_id) VALUES (:userId, :groupId)", nativeQuery = true)
    void addMemberToFamily(@Param("userId") long userId, @Param("groupId") long groupId);

    /**
     * Usuwa użytkownika z grupy rodzinnej.
     *
     * @param User_id ID użytkownika do usunięcia
     * @param FamilyGroupId ID grupy rodzinnej
     */
    @Modifying
    @Query(value = "DELETE FROM user_group_link WHERE user_id = :userId AND group_id = :groupId", nativeQuery = true)
    void deleteMemberFromFamilyGroup(long User_id, long FamilyGroupId);

}
