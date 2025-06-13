package com.backend.legacybookbackend.Services;

import com.backend.legacybookbackend.DTO.PostResponse;
import com.backend.legacybookbackend.Model.Post;
import com.backend.legacybookbackend.Model.PostRepository;
import com.backend.legacybookbackend.Model.User;
import com.backend.legacybookbackend.Model.UserRepository;
import com.backend.legacybookbackend.DTO.CreatePostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    // Katalog do zapisywania plików uploadowanych przez użytkowników
    private final String uploadDir = "uploads/posts/";
    // Podstawowy URL serwera, przydatny do generowania pełnych linków do plików
    private final String baseUrl = "http://10.0.2.2:8080";

    // Pobiera wszystkie posty posortowane malejąco po dacie utworzenia i mapuje je na DTO PostResponse
    public List<PostResponse> getMainFeed() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(post -> {
            PostResponse response = new PostResponse();
            response.setId(post.getId());
            response.setContent(post.getContent());
            // Jeśli jest ścieżka do obrazka/audio, dodaj URL bazowy, aby uzyskać pełny adres
            response.setImagePath(post.getImagePath() != null ? baseUrl + post.getImagePath() : null);
            response.setAudioPath(post.getAudioPath() != null ? baseUrl + post.getAudioPath() : null);
            response.setAuthorName(post.getAuthor().getName());
            response.setCreatedAt(post.getCreatedAt().toString());
            return response;
        }).collect(Collectors.toList());
    }

    // Tworzy nowy post przypisany do użytkownika, zapisuje pliki jeśli są przesłane
    @Transactional
    public Post createPost(String userEmail, CreatePostRequest req, MultipartFile image, MultipartFile audio) throws IOException {
        // Znajdź użytkownika po mailu lub rzuć wyjątek, jeśli nie ma takiego
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setAuthor(author);
        post.setContent(req.getContent());
        post.setCreatedAt(LocalDateTime.now());

        // Jeśli przesłano obrazek, zapisz go i ustaw ścieżkę w poście
        if (image != null && !image.isEmpty()) {
            String imagePath = saveFile(image, "images");
            post.setImagePath(imagePath);
        }

        // Jeśli przesłano plik audio, zapisz go i ustaw ścieżkę w poście
        if (audio != null && !audio.isEmpty()) {
            String audioPath = saveFile(audio, "audio");
            post.setAudioPath(audioPath);
        }

        // Zapisz post w repozytorium (bazie)
        return postRepository.save(post);
    }

    // Metoda pomocnicza do zapisywania plików na dysku w określonym podfolderze
    private String saveFile(MultipartFile file, String subDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir + subDir);
        // Tworzy katalog, jeśli nie istnieje
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // Generuje unikalną nazwę pliku na podstawie aktualnego czasu i oryginalnej nazwy pliku
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        // Kopiuje zawartość przesłanego pliku do lokalizacji na dysku
        Files.copy(file.getInputStream(), filePath);
        // Zwraca względną ścieżkę, która później będzie dołączana do baseUrl
        return "/posts/" + subDir + "/" + fileName;
    }
}
