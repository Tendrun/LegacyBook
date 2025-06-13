package com.backend.legacybookbackend.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Encja reprezentująca post na platformie.
 * Zawiera treść, autora, opcjonalne media (obraz, audio) oraz datę utworzenia.
 */
@Entity
@Table(name = "posts")
public class Post {

    /**
     * Unikalny identyfikator posta.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Autor posta - użytkownik, który go stworzył.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    /**
     * Treść posta, maksymalnie 1000 znaków.
     */
    @Column(nullable = false, length = 1000)
    private String content;

    /**
     * Ścieżka do załączonego zdjęcia (opcjonalna).
     */
    @Column
    private String imagePath;

    /**
     * Ścieżka do załączonego nagrania audio (opcjonalna).
     */
    @Column
    private String audioPath;

    /**
     * Data i czas utworzenia posta.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Konstruktor domyślny.
     */
    public Post() {}

    /**
     * Pobiera unikalny identyfikator posta.
     * @return id posta
     */
    public Long getId() { return id; }

    /**
     * Pobiera autora posta.
     * @return autor (użytkownik)
     */
    public User getAuthor() { return author; }

    /**
     * Ustawia autora posta.
     * @param author użytkownik tworzący post
     */
    public void setAuthor(User author) { this.author = author; }

    /**
     * Pobiera treść posta.
     * @return zawartość tekstowa posta
     */
    public String getContent() { return content; }

    /**
     * Ustawia treść posta.
     * @param content zawartość tekstowa
     */
    public void setContent(String content) { this.content = content; }

    /**
     * Pobiera ścieżkę do zdjęcia.
     * @return ścieżka do obrazu lub null, jeśli brak
     */
    public String getImagePath() { return imagePath; }

    /**
     * Ustawia ścieżkę do zdjęcia.
     * @param imagePath ścieżka do obrazu
     */
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    /**
     * Pobiera ścieżkę do nagrania audio.
     * @return ścieżka do audio lub null, jeśli brak
     */
    public String getAudioPath() { return audioPath; }

    /**
     * Ustawia ścieżkę do nagrania audio.
     * @param audioPath ścieżka do pliku audio
     */
    public void setAudioPath(String audioPath) { this.audioPath = audioPath; }

    /**
     * Pobiera datę i czas utworzenia posta.
     * @return data i czas utworzenia
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Ustawia datę i czas utworzenia posta.
     * @param createdAt data i czas utworzenia
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
