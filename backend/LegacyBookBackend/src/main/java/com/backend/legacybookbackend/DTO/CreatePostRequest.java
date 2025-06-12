package com.backend.legacybookbackend.DTO;

import org.springframework.web.multipart.MultipartFile;

/**
 * DTO reprezentujący dane do utworzenia nowego wpisu (posta).
 * Zawiera treść wpisu oraz opcjonalne pliki multimedialne: obraz i nagranie audio.
 */
public class CreatePostRequest {

    /**
     * Treść wpisu.
     */
    private String content;

    /**
     * Obraz powiązany z wpisem.
     */
    private MultipartFile image;

    /**
     * Nagranie audio powiązane z wpisem.
     */
    private MultipartFile audio;

    /**
     * Pobiera treść wpisu.
     * @return treść wpisu
     */
    public String getContent() { return content; }

    /**
     * Ustawia treść wpisu.
     * @param content treść wpisu
     */
    public void setContent(String content) { this.content = content; }

    /**
     * Pobiera plik obrazu.
     * @return plik obrazu
     */
    public MultipartFile getImage() { return image; }

    /**
     * Ustawia plik obrazu.
     * @param image plik obrazu
     */
    public void setImage(MultipartFile image) { this.image = image; }

    /**
     * Pobiera plik audio.
     * @return plik audio
     */
    public MultipartFile getAudio() { return audio; }

    /**
     * Ustawia plik audio.
     * @param audio plik audio
     */
    public void setAudio(MultipartFile audio) { this.audio = audio; }
}
