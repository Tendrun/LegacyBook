package com.example.legacykeep.model;

/**
 * Model reprezentujący post w aplikacji.
 * Przechowuje informacje o treści, ścieżkach do multimediów, autorze oraz dacie utworzenia.
 */
public class PostModel {
    /**
     * Unikalny identyfikator posta.
     */
    private long id;

    /**
     * Treść posta.
     */
    private String content;

    /**
     * Ścieżka do obrazu powiązanego z postem.
     */
    private String imagePath;

    /**
     * Ścieżka do pliku audio powiązanego z postem.
     */
    private String audioPath;

    /**
     * Nazwa autora posta.
     */
    private String authorName;

    /**
     * Data utworzenia posta.
     */
    private String createdAt;

    /**
     * Zwraca identyfikator posta.
     *
     * @return identyfikator posta
     */
    public long getId() { return id; }

    /**
     * Ustawia identyfikator posta.
     *
     * @param id identyfikator posta
     */
    public void setId(long id) { this.id = id; }

    /**
     * Zwraca treść posta.
     *
     * @return treść posta
     */
    public String getContent() { return content; }

    /**
     * Ustawia treść posta.
     *
     * @param content treść posta
     */
    public void setContent(String content) { this.content = content; }

    /**
     * Zwraca ścieżkę do obrazu.
     *
     * @return ścieżka do obrazu
     */
    public String getImagePath() { return imagePath; }

    /**
     * Ustawia ścieżkę do obrazu.
     *
     * @param imagePath ścieżka do obrazu
     */
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    /**
     * Zwraca ścieżkę do pliku audio.
     *
     * @return ścieżka do pliku audio
     */
    public String getAudioPath() { return audioPath; }

    /**
     * Ustawia ścieżkę do pliku audio.
     *
     * @param audioPath ścieżka do pliku audio
     */
    public void setAudioPath(String audioPath) { this.audioPath = audioPath; }

    /**
     * Zwraca nazwę autora posta.
     *
     * @return nazwa autora
     */
    public String getAuthorName() { return authorName; }

    /**
     * Ustawia nazwę autora posta.
     *
     * @param authorName nazwa autora
     */
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    /**
     * Zwraca datę utworzenia posta.
     *
     * @return data utworzenia
     */
    public String getCreatedAt() { return createdAt; }

    /**
     * Ustawia datę utworzenia posta.
     *
     * @param createdAt data utworzenia
     */
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}