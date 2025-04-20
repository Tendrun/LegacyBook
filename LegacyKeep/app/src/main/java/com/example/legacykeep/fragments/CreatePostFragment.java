package com.example.legacykeep.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.legacykeep.R;

public class CreatePostFragment extends Fragment {

    private Button uploadPhotoButton, recordVoiceButton, pickLocationButton, submitPostButton;
    private EditText descriptionField;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uploadPhotoButton = view.findViewById(R.id.buttonUploadPhoto);
        recordVoiceButton = view.findViewById(R.id.buttonRecordVoice);
        pickLocationButton = view.findViewById(R.id.buttonPickLocation);
        submitPostButton = view.findViewById(R.id.buttonSubmitPost);
        descriptionField = view.findViewById(R.id.editTextDescription);

        // Placeholder actions (you’ll implement real ones later)
        uploadPhotoButton.setOnClickListener(v -> {
            // TODO: Open camera/gallery (coming next)
        });

        recordVoiceButton.setOnClickListener(v -> {
            // TODO: Start audio recording (placeholder)
        });

        pickLocationButton.setOnClickListener(v -> {
            // TODO: Launch map picker (future step)
        });

        submitPostButton.setOnClickListener(v -> {
            String description = descriptionField.getText().toString().trim();
            // TODO: Validate and save post
        });
    }
}
