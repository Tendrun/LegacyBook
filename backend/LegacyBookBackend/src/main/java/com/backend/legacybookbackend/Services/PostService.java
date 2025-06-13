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

/**
 * Serwis odpowiedzialny za operacje związane z postami użytkowników.
 * Obsługuje tworzenie postów oraz pobieranie głównego feedu.
 */
@Service
public class PostService {

    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;

    /** Ścieżka do katalogu, w którym zapisywane są pliki postów. */
    private final String uploadDir = "uploads/posts/";

    /** Bazowy adres URL używany do tworzenia pełnych ścieżek plików. */
    private final String baseUrl = "http://10.0.2.2:8080";

    /**
     * Zwraca listę wszystkich postów posortowanych malejąco według daty utworzenia.
     * <p>
     * Każdy post konwertowany jest do obiektu {@link PostResponse},
     * zawierającego podstawowe informacje do wyświetlenia w głównym feedzie aplikacji.
     *
     * @return lista postów w postaci DTO {@link PostResponse}
     */
    public List<PostResponse> getMainFeed() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(post -> {
            PostResponse response = new PostResponse();
            response.setId(post.getId());
            response.setContent(post.getContent());
            response.setImagePath(post.getImagePath() != null ? baseUrl + post.getImagePath() : null);
            response.setAudioPath(post.getAudioPath() != null ? baseUrl + post.getAudioPath() : null);
            response.setAuthorName(post.getAuthor().getName());
            response.setCreatedAt(post.getCreatedAt().toString());
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * Tworzy nowy post użytkownika na podstawie danych wejściowych i opcjonalnych plików.
     * <p>
     * Zapisuje przesłane pliki obrazu i dźwięku do odpowiednich katalogów,
     * przypisuje autora i ustala czas utworzenia posta.
     *
     * @param userEmail adres e-mail autora posta
     * @param req dane tekstowe posta (np. treść)
     * @param image plik obrazu (może być null)
     * @param audio plik audio (może być null)
     * @return zapisany post
     * @throws IOException w przypadku błędu przy zapisie plików
     * @throws RuntimeException jeśli użytkownik nie zostanie odnaleziony
     */
    @Transactional
    public Post createPost(String userEmail, CreatePostRequest req, MultipartFile image, MultipartFile audio) throws IOException {
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setAuthor(author);
        post.setContent(req.getContent());
        post.setCreatedAt(LocalDateTime.now());

        // Zapis pliku obrazu, jeśli istnieje
        if (image != null && !image.isEmpty()) {
            String imagePath = saveFile(image, "images");
            post.setImagePath(imagePath);
        }

        // Zapis pliku audio, jeśli istnieje
        if (audio != null && !audio.isEmpty()) {
            String audioPath = saveFile(audio, "audio");
            post.setAudioPath(audioPath);
        }

        return postRepository.save(post);
    }

    /**
     * Zapisuje przekazany plik w określonym podkatalogu w katalogu uploadów.
     *
     * @param file plik do zapisania
     * @param subDir podkatalog (np. "images" lub "audio")
     * @return względna ścieżka dostępu do zapisanego pliku
     * @throws IOException w przypadku błędu zapisu
     */
    private String saveFile(MultipartFile file, String subDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir + subDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return "/posts/" + subDir + "/" + fileName; // Zwraca skróconą ścieżkę względną
    }

    /**
     * Konstruktor klasy {@code PostService} inicjalizujący zależności do repozytoriów.
     *
     * @param postRepository repozytorium postów
     * @param userRepository repozytorium użytkowników
     */
    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
}
