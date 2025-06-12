package com.backend.legacybookbackend.DTO;

/**
 * DTO reprezentujące dane odpowiedzi dla posta.
 */
public class PostResponse {

    /**
     * Unikalny identyfikator posta.
     */
    private Long id;

    /**
     * Treść posta.
     */
    private String content;

    /**
     * Ścieżka do obrazka powiązanego z postem (jeśli istnieje).
     */
    private String imagePath;

    /**
     * Ścieżka do nagrania audio powiązanego z postem (jeśli istnieje).
     */
    private String audioPath;

    /**
     * Nazwa autora posta.
     */
    private String authorName;

    /**
     * Data i czas utworzenia posta w formacie tekstowym.
     */
    private String createdAt;

    // Gettery i settery

    /**
     * Pobiera ID posta.
     * @return id posta
     */
    public Long getId() { return id; }

    /**
     * Ustawia ID posta.
     * @param id id posta
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Pobiera treść posta.
     * @return treść posta
     */
    public String getContent() { return content; }

    /**
     * Ustawia treść posta.
     * @param content treść posta
     */
    public void setContent(String content) { this.content = content; }

    /**
     * Pobiera ścieżkę do obrazka.
     * @return ścieżka do obrazka
     */
    public String getImagePath() { return imagePath; }

    /**
     * Ustawia ścieżkę do obrazka.
     * @param imagePath ścieżka do obrazka
     */
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    /**
     * Pobiera ścieżkę do nagrania audio.
     * @return ścieżka do nagrania audio
     */
    public String getAudioPath() { return audioPath; }

    /**
     * Ustawia ścieżkę do nagrania audio.
     * @param audioPath ścieżka do nagrania audio
     */
    public void setAudioPath(String audioPath) { this.audioPath = audioPath; }

    /**
     * Pobiera nazwę autora posta.
     * @return nazwa autora
     */
    public String getAuthorName() { return authorName; }

    /**
     * Ustawia nazwę autora posta.
     * @param authorName nazwa autora
     */
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    /**
     * Pobiera datę i czas utworzenia posta.
     * @return data i czas utworzenia
     */
    public String getCreatedAt() { return createdAt; }

    /**
     * Ustawia datę i czas utworzenia posta.
     * @param createdAt data i czas utworzenia
     */
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
