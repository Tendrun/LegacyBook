package com.backend.legacybookbackend.Controller;

import com.backend.legacybookbackend.DTO.CreatePostRequest;
import com.backend.legacybookbackend.Model.Post;
import com.backend.legacybookbackend.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired private PostService postService;

    // 1) główny feed
    @GetMapping
    public ResponseEntity<List<Post>> getFeed() {
        return ResponseEntity.ok(postService.getMainFeed());
    }

    // 2) dodawanie posta (używa JWT do odczytu bieżącego użytkownika)
    @PostMapping
    public ResponseEntity<Post> addPost(
            @RequestBody CreatePostRequest req,
            Authentication authentication) {
        String userEmail = authentication.getName();
        Post saved = postService.createPost(userEmail, req);
        return ResponseEntity.ok(saved);
    }
}
