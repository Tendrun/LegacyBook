package com.backend.legacybookbackend.Controller;

import com.backend.legacybookbackend.DTO.CreatePostRequest;    // DTO zawierające dane nowego posta
import com.backend.legacybookbackend.DTO.PostResponse;       // DTO zwracające dane posta
import com.backend.legacybookbackend.Services.PostService;   // serwis obsługujący logikę postów
import org.springframework.beans.factory.annotation.Autowired; // automatyczne wstrzykiwanie beana
import org.springframework.http.ResponseEntity;              // typ odpowiedzi HTTP
import org.springframework.security.core.Authentication;     // obiekt uwierzytelnienia
import org.springframework.web.bind.annotation.*;           // adnotacje REST
import org.springframework.web.multipart.MultipartFile;      // obsługa plików multipart

import java.io.IOException;                                 // obsługa wyjątków I/O
import java.util.List;                                      // lista wyników

@RestController                                              // definiuje kontroler REST
@RequestMapping("/api/posts")                                // bazowy URL dla operacji na postach
public class PostController {

    @Autowired
    private PostService postService;                        // wstrzyknięcie serwisu PostService

    @PostMapping(consumes = {"multipart/form-data"})        // POST /api/posts z multipart form
    public ResponseEntity<PostResponse> addPost(
            @RequestPart("post") CreatePostRequest req,     // część „post” z danymi JSON
            @RequestPart(value = "image", required = false) MultipartFile image, // opcjonalne zdjęcie
            @RequestPart(value = "audio", required = false) MultipartFile audio, // opcjonalne audio
            Authentication authentication) throws IOException {
        String userEmail = authentication.getName();         // pobierz email zalogowanego użytkownika
        postService.createPost(userEmail, req, image, audio); // wywołaj serwis tworzący post
        return ResponseEntity.ok().build();                  // zwróć HTTP 200 OK bez ciała
    }

    @GetMapping                                            // GET /api/posts
    public ResponseEntity<List<PostResponse>> getFeed() {
        List<PostResponse> feed = postService.getMainFeed(); // pobierz główny feed
        return ResponseEntity.ok(feed);                     // zwróć listę postów z HTTP 200 OK
    }
}
