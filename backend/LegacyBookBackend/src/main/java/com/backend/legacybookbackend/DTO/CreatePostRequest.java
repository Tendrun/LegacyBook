package com.backend.legacybookbackend.DTO;

import org.springframework.web.multipart.MultipartFile;

public class CreatePostRequest {
    private String content;
    private MultipartFile image;
    private MultipartFile audio;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public MultipartFile getImage() { return image; }
    public void setImage(MultipartFile image) { this.image = image; }

    public MultipartFile getAudio() { return audio; }
    public void setAudio(MultipartFile audio) { this.audio = audio; }
}