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

/**
 * Kontroler REST do obsługi operacji związanych z postami.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Konstruktor kontrolera.
     *
     * @param postService serwis obsługujący logikę biznesową postów
     */
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Endpoint do tworzenia nowego posta. Obsługuje przesyłanie treści posta oraz opcjonalnie obrazów i nagrań audio.
     *
     * @param req obiekt z danymi posta (treść)
     * @param image opcjonalny plik obrazu do dodania do posta
     * @param audio opcjonalny plik audio do dodania do posta
     * @param authentication obiekt uwierzytelnienia użytkownika, z którego pobierany jest email
     * @return pusty ResponseEntity z kodem 200 OK po pomyślnym utworzeniu posta
     * @throws IOException jeśli wystąpi problem z obsługą plików
     */
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

    /**
     * Endpoint do pobierania listy postów w głównym feedzie, posortowanych od najnowszych.
     *
     * @return ResponseEntity z listą PostResponse i kodem 200 OK
     */
    @GetMapping
    public ResponseEntity<List<PostResponse>> getFeed() {
        return ResponseEntity.ok(postService.getMainFeed());
    }
}
