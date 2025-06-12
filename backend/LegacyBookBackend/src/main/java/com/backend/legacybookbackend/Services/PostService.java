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

    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;

    private final String uploadDir = "uploads/posts/";
    private final String baseUrl = "http://10.0.2.2:8080";

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

    @Transactional
    public Post createPost(String userEmail, CreatePostRequest req, MultipartFile image, MultipartFile audio) throws IOException {
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setAuthor(author);
        post.setContent(req.getContent());
        post.setCreatedAt(LocalDateTime.now());

        // Save image
        if (image != null && !image.isEmpty()) {
            String imagePath = saveFile(image, "images");
            post.setImagePath(imagePath);
        }

        // Save audio
        if (audio != null && !audio.isEmpty()) {
            String audioPath = saveFile(audio, "audio");
            post.setAudioPath(audioPath);
        }

        return postRepository.save(post);
    }

    private String saveFile(MultipartFile file, String subDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir + subDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return "/posts/" + subDir + "/" + fileName; // Return shortened relative path
    }

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
}