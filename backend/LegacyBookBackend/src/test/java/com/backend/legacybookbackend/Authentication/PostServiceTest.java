package com.backend.legacybookbackend.Authentication;

import com.backend.legacybookbackend.DTO.CreatePostRequest;
import com.backend.legacybookbackend.Model.Post;
import com.backend.legacybookbackend.Model.PostRepository;
import com.backend.legacybookbackend.Model.User;
import com.backend.legacybookbackend.Model.UserRepository;
import com.backend.legacybookbackend.Services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private PostRepository postRepository;
    private UserRepository userRepository;
    private PostService postService;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        userRepository = mock(UserRepository.class);
        postService = new PostService(postRepository, userRepository);
    }
    @Test
    void testCreatePost() throws IOException {
        // Mock danych wejściowych
        String userEmail = "test@example.com";
        CreatePostRequest request = new CreatePostRequest();
        request.setContent("Test content");

        MultipartFile image = mock(MultipartFile.class);
        MultipartFile audio = mock(MultipartFile.class);

        // Konfiguracja mocka dla pliku obrazu
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("test-image.jpg");
        when(image.getInputStream()).thenReturn(new ByteArrayInputStream("image data".getBytes()));

        // Konfiguracja mocka dla pliku audio
        when(audio.isEmpty()).thenReturn(false);
        when(audio.getOriginalFilename()).thenReturn("test-audio.mp3");
        when(audio.getInputStream()).thenReturn(new ByteArrayInputStream("audio data".getBytes()));

        // Mock użytkownika
        User user = new User();
        user.setEmail(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        // Mock zapisu posta
        Post savedPost = new Post();
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // Wywołanie metody
        Post result = postService.createPost(userEmail, request, image, audio);

        // Weryfikacja
        assertNotNull(result);
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(postRepository, times(1)).save(any(Post.class));

        // Sprawdzenie, czy post zawiera poprawne dane
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postCaptor.capture());
        Post capturedPost = postCaptor.getValue();
        assertEquals("Test content", capturedPost.getContent());
        assertEquals(user, capturedPost.getAuthor());
        assertNotNull(capturedPost.getCreatedAt());
    }
}