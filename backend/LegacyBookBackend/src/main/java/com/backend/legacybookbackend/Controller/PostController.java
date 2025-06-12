package com.backend.legacybookbackend.Controller;

import com.backend.legacybookbackend.DTO.CreatePostRequest;
import com.backend.legacybookbackend.DTO.PostResponse;
import com.backend.legacybookbackend.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired private PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<PostResponse> addPost(
            @RequestPart("post") CreatePostRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "audio", required = false) MultipartFile audio,
            Authentication authentication) throws IOException {
        String userEmail = authentication.getName();
        postService.createPost(userEmail, req, image, audio);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getFeed() {
        return ResponseEntity.ok(postService.getMainFeed());
    }
}