package com.backend.legacybookbackend.Services;

import com.backend.legacybookbackend.Model.Post;
import com.backend.legacybookbackend.Model.PostRepository;
import com.backend.legacybookbackend.Model.User;
import com.backend.legacybookbackend.Model.UserRepository;
import com.backend.legacybookbackend.DTO.CreatePostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;

    public List<Post> getMainFeed() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Post createPost(String userEmail, CreatePostRequest req) {
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = new Post();
        post.setAuthor(author);
        post.setContent(req.getContent());
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }
}
