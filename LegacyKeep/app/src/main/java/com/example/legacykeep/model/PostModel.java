package com.example.legacykeep.model;

public class PostModel {
    private int imageResId;
    private String description;
    private String location;

    public PostModel(int imageResId, String description, String location) {
        this.imageResId = imageResId;
        this.description = description;
        this.location = location;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }
}
