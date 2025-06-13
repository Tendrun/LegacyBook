package com.backend.legacybookbackend.Authentication;

import com.backend.legacybookbackend.Controller.PostController;
import com.backend.legacybookbackend.DTO.CreatePostRequest;
import com.backend.legacybookbackend.DTO.PostResponse;
import com.backend.legacybookbackend.Services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostControllerTest {

    private PostController postController;

    @Mock
    private PostService postService;

    @Mock
    private Authentication authentication;

    @Mock
    private MultipartFile image;

    @Mock
    private MultipartFile audio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postController = new PostController(postService);
    }

    @Test
    void testAddPost_Success() throws IOException {
        // Arrange
        CreatePostRequest request = new CreatePostRequest();
        request.setContent("Test content");
        when(authentication.getName()).thenReturn("test@example.com");

        // Act
        ResponseEntity<PostResponse> response = postController.addPost(request, image, audio, authentication);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(postService, times(1)).createPost("test@example.com", request, image, audio);
    }

    @Test
    void testGetFeed_Success() {
        // Arrange
        PostResponse post1 = new PostResponse();
        post1.setId(1L);
        post1.setContent("Post 1");
        PostResponse post2 = new PostResponse();
        post2.setId(2L);
        post2.setContent("Post 2");

        when(postService.getMainFeed()).thenReturn(List.of(post1, post2));

        // Act
        ResponseEntity<List<PostResponse>> response = postController.getFeed();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(postService, times(1)).getMainFeed();
    }
}