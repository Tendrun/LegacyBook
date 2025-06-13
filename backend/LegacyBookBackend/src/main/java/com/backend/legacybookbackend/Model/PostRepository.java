package com.backend.legacybookbackend.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repozytorium dla encji Post, umożliwia operacje CRUD
public interface PostRepository extends JpaRepository<Post, Long> {

    // Zwraca wszystkie posty posortowane malejąco wg daty utworzenia (najnowsze pierwsze)
    List<Post> findAllByOrderByCreatedAtDesc();
}
