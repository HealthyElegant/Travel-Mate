package io.github.project_travel_mate.tripdiary;

import android.net.Uri;

public class TripDiaryEntry {
    private Uri imageUri;
    private String text;

    public TripDiaryEntry(Uri imageUri, String text) {
        this.imageUri = imageUri;
        this.text = text;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getText() {
        return text;
    }
}
