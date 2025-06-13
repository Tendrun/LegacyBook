package com.backend.legacybookbackend.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts") // Mapa encji na tabelę "posts"
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoinkrementowane ID
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Relacja wiele postów -> jeden użytkownik
    @JoinColumn(name = "user_id", nullable = false) // Klucz obcy do autora posta
    private User author;

    @Column(nullable = false, length = 1000) // Zawartość tekstowa posta (do 1000 znaków)
    private String content;

    @Column // Opcjonalna ścieżka do załączonego zdjęcia
    private String imagePath;

    @Column // Opcjonalna ścieżka do załączonego nagrania głosowego
    private String audioPath;

    @Column(nullable = false) // Data utworzenia posta
    private LocalDateTime createdAt;

    public Post() {}

    // Gettery i settery
    public Long getId() { return id; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getAudioPath() { return audioPath; }
    public void setAudioPath(String audioPath) { this.audioPath = audioPath; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
