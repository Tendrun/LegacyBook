package com.example.legacykeep.DTO;

/**
 * Reprezentuje żądanie utworzenia nowego posta.
 * Zawiera treść posta przekazywaną przez klienta.
 */
public class CreatePostRequest {
    /**
     * Treść posta.
     */
    private String content;

    /**
     * Zwraca treść posta.
     *
     * @return treść posta jako {@code String}
     */
    public String getContent() {
        return content;
    }

    /**
     * Ustawia treść posta.
     *
     * @param content treść posta do ustawienia
     */
    public void setContent(String content) {
        this.content = content;
    }
}