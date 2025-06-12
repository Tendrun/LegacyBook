package com.backend.legacybookbackend.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Interfejs repozytorium dla encji Post.
 * Rozszerza JpaRepository, zapewniając podstawowe operacje CRUD.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Znajduje wszystkie posty, sortując je malejąco według daty utworzenia.
     *
     * @return lista postów posortowana od najnowszych do najstarszych
     */
    List<Post> findAllByOrderByCreatedAtDesc();
}
