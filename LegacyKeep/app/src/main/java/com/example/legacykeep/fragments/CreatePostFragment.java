// app/src/main/java/com/example/legacykeep/fragments/CreatePostFragment.java
package com.example.legacykeep.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.legacykeep.R;
import com.example.legacykeep.model.PostModel;
import com.example.legacykeep.viewmodel.SharedPostViewModel;

public class CreatePostFragment extends Fragment {

    private SharedPostViewModel viewModel;
    private EditText descriptionField;
    private Button uploadPhotoButton, recordVoiceButton, pickLocationButton, submitPostButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pobranie Shared ViewModel
        viewModel = new ViewModelProvider(requireActivity())
                .get(SharedPostViewModel.class);

        // Inicjalizacja widoków
        descriptionField     = view.findViewById(R.id.editTextDescription);
        uploadPhotoButton    = view.findViewById(R.id.buttonUploadPhoto);
        recordVoiceButton    = view.findViewById(R.id.buttonRecordVoice);
        pickLocationButton   = view.findViewById(R.id.buttonPickLocation);
        submitPostButton     = view.findViewById(R.id.buttonSubmitPost);

        // TODO: implementacja pozostałych przycisków
        uploadPhotoButton.setOnClickListener(v -> {
            // TODO: Open camera/gallery
        });
        recordVoiceButton.setOnClickListener(v -> {
            // TODO: Start audio recording
        });
        pickLocationButton.setOnClickListener(v -> {
            // TODO: Launch map picker
        });

        // Obsługa dodawania posta
        submitPostButton.setOnClickListener(v -> {
            String desc = descriptionField.getText().toString().trim();
            if (desc.isEmpty()) {
                descriptionField.setError("Opis nie może być pusty");
                return;
            }
            // Tworzymy PostModel i dodajemy do ViewModelu
            PostModel newPost = new PostModel(
                    R.drawable.logo_legacykeep,
                    desc,
                    "Twoja lokalizacja"
            );
            viewModel.addPost(newPost);

            descriptionField.setText("");
            Toast.makeText(getContext(), "Dodano post!", Toast.LENGTH_SHORT).show();
        });
    }
}
