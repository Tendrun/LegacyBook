package com.backend.legacybookbackend.DTO;

import org.springframework.web.multipart.MultipartFile; // typ plików do uploadu w multipart

public class CreatePostRequest {
    private String content;           // treść posta
    private MultipartFile image;      // opcjonalne zdjęcie do posta
    private MultipartFile audio;      // opcjonalne nagranie audio do posta

    public String getContent() {      // getter dla treści posta
        return content;
    }
    public void setContent(String content) { // setter dla treści posta
        this.content = content;
    }

    public MultipartFile getImage() { // getter dla zdjęcia posta
        return image;
    }
    public void setImage(MultipartFile image) { // setter dla zdjęcia posta
        this.image = image;
    }

    public MultipartFile getAudio() { // getter dla audio posta
        return audio;
    }
    public void setAudio(MultipartFile audio) { // setter dla audio posta
        this.audio = audio;
    }
}
