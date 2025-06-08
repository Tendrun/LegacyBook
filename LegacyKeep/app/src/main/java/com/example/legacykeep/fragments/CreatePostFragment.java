// app/src/main/java/com/example/legacykeep/fragments/CreatePostFragment.java
package com.example.legacykeep.fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.legacykeep.R;
import com.example.legacykeep.model.PostModel;
import com.example.legacykeep.viewmodel.SharedPostViewModel;

import java.io.IOException;

public class CreatePostFragment extends Fragment {

    private SharedPostViewModel viewModel;
    private EditText descriptionField;
    private Button uploadPhotoButton, recordVoiceButton, pickLocationButton, submitPostButton;

    private Uri photoUri, audioUri;
    private MediaRecorder recorder;
    private boolean isRecording = false;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        photoUri = uri;
                        Toast.makeText(getContext(), "Wybrano zdjęcie", Toast.LENGTH_SHORT).show();
                    });

    private final ActivityResultLauncher<String> permissionAudioLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    granted -> {
                        if (granted) toggleRecording();
                        else Toast.makeText(getContext(), "Brak uprawnień audio", Toast.LENGTH_SHORT).show();
                    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        super.onViewCreated(v, b);

        viewModel = new ViewModelProvider(requireActivity())
                .get(SharedPostViewModel.class);

        descriptionField   = v.findViewById(R.id.editTextDescription);
        uploadPhotoButton  = v.findViewById(R.id.buttonUploadPhoto);
        recordVoiceButton  = v.findViewById(R.id.buttonRecordVoice);
        pickLocationButton = v.findViewById(R.id.buttonPickLocation);
        submitPostButton   = v.findViewById(R.id.buttonSubmitPost);

        uploadPhotoButton.setOnClickListener(btn -> pickImageLauncher.launch("image/*"));

        recordVoiceButton.setOnClickListener(btn -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {
                permissionAudioLauncher.launch(Manifest.permission.RECORD_AUDIO);
            } else {
                toggleRecording();
            }
        });

        pickLocationButton.setOnClickListener(btn ->
                Toast.makeText(getContext(), "Wybierz lokalizację (TODO)", Toast.LENGTH_SHORT).show()
        );

        submitPostButton.setOnClickListener(btn -> {
            String desc = descriptionField.getText().toString().trim();
            if (desc.isEmpty()) {
                descriptionField.setError("Opis nie może być pusty");
                return;
            }
            viewModel.addPost(new PostModel(photoUri, desc, "Twoja lokalizacja", audioUri));

            // reset
            descriptionField.setText("");
            photoUri = null;
            audioUri = null;
            if (recorder != null) {
                recorder.release();
                recorder = null;
                isRecording = false;
                recordVoiceButton.setText("Nagraj głos");
            }
            Toast.makeText(getContext(), "Dodano post!", Toast.LENGTH_SHORT).show();
        });
    }

    private void toggleRecording() {
        if (!isRecording) {
            try {
                ContentValues vals = new ContentValues();
                vals.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp4");
                audioUri = requireContext().getContentResolver()
                        .insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, vals);

                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                recorder.setOutputFile(
                        requireContext().getContentResolver()
                                .openFileDescriptor(audioUri, "rw").getFileDescriptor()
                );
                recorder.prepare();
                recorder.start();

                isRecording = true;
                recordVoiceButton.setText("Zatrzymaj nagrywanie");
                Toast.makeText(getContext(), "Nagrywanie...", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            recorder.stop();
            recorder.release();
            isRecording = false;
            recordVoiceButton.setText("Nagraj głos");
            Toast.makeText(getContext(), "Nagrano audio", Toast.LENGTH_SHORT).show();
        }
    }
}
