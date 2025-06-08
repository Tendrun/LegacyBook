// app/src/main/java/com/example/legacykeep/model/PostModel.java
package com.example.legacykeep.model;

import android.net.Uri;

public class PostModel {
    private final Uri imageUri;     // może być null
    private final String description;
    private final String location;
    private final Uri audioUri;     // może być null

    public PostModel(Uri imageUri, String description, String location, Uri audioUri) {
        this.imageUri    = imageUri;
        this.description = description;
        this.location    = location;
        this.audioUri    = audioUri;
    }

    public Uri getImageUri()       { return imageUri; }
    public String getDescription() { return description; }
    public String getLocation()    { return location; }
    public Uri getAudioUri()       { return audioUri; }
}
