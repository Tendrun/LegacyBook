// Backend: PostResponse.java
package com.backend.legacybookbackend.DTO;

public class PostResponse {
    private Long id;             // unikalne ID posta
    private String content;      // treść posta
    private String imagePath;    // ścieżka do załączonego obrazka
    private String audioPath;    // ścieżka do załączonego pliku audio
    private String authorName;   // nazwa/autorskie imię tworzącego post
    private String createdAt;    // data i czas utworzenia posta

    // Getter i setter dla ID posta
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // Getter i setter dla treści posta
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    // Getter i setter dla ścieżki obrazka
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    // Getter i setter dla ścieżki audio
    public String getAudioPath() { return audioPath; }
    public void setAudioPath(String audioPath) { this.audioPath = audioPath; }

    // Getter i setter dla nazwy autora
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    // Getter i setter dla daty utworzenia
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
