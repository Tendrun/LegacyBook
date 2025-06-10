// app/src/main/java/com/example/legacykeep/fragments/CreatePostFragment.java
package com.example.legacykeep.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.media.MediaRecorder;
import java.io.File;
import java.io.IOException;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.DTO.CreatePostRequest;
import com.example.legacykeep.R;
import com.example.legacykeep.model.PostModel;
import com.example.legacykeep.viewmodel.SharedPostViewModel;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostFragment extends Fragment {

    private SharedPostViewModel viewModel;
    private EditText descriptionField;
    private Button uploadPhotoButton, recordVoiceButton, pickLocationButton, submitPostButton;
    private static final int GALLERY_REQUEST_CODE = 1001;
    private static final int AUDIO_REQUEST_CODE = 2001;
    private static final int REQUEST_MICROPHONE_PERMISSION = 3001;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private Uri selectedImageUri = null;
    private Uri selectedAudioUri = null;
    private Button playAudioButton;
    private MediaPlayer mediaPlayer;
    private Uri recordedAudioUri = null;
    private Button stopRecordingButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        descriptionField = view.findViewById(R.id.editTextDescription);
        uploadPhotoButton = view.findViewById(R.id.buttonUploadPhoto);
        recordVoiceButton = view.findViewById(R.id.buttonRecordVoice);
        playAudioButton = view.findViewById(R.id.buttonPlayAudio);
        submitPostButton = view.findViewById(R.id.buttonSubmitPost);
        stopRecordingButton = view.findViewById(R.id.buttonStopRecording); // Ensure this is initialized

        // Handle photo upload
        uploadPhotoButton.setOnClickListener(v -> openGallery());

        // Handle audio recording
        recordVoiceButton.setOnClickListener(v -> startAudioRecording());
        stopRecordingButton.setOnClickListener(v -> stopAudioRecording());
        playAudioButton.setOnClickListener(v -> playRecordedAudio());
        submitPostButton.setOnClickListener(v -> submitPost());

        // Handle post submission
        submitPostButton.setOnClickListener(v -> submitPost());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }



    private void submitPost() {
        String content = descriptionField.getText().toString().trim();
        if (content.isEmpty()) {
            descriptionField.setError("Content cannot be empty");
            return;
        }

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to create a post", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare JSON part
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setContent(content);
        RequestBody postRequestBody = RequestBody.create(
                MediaType.parse("application/json"),
                new Gson().toJson(createPostRequest)
        );

        // Prepare image part
        MultipartBody.Part imagePart = null;
        if (selectedImageUri != null) {
            imagePart = prepareFilePart("image", selectedImageUri);
        }

        // Prepare audio part
        MultipartBody.Part audioPart = null;
        if (recordedAudioUri != null) {
            audioPart = prepareFilePart("audio", recordedAudioUri);
        }

        ApiService apiService = ApiClient.getApiService();
        Call<PostModel> call = apiService.createPost("Bearer " + authToken, postRequestBody, imagePart, audioPart);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.isSuccessful()) {
                    Log.i("CreatePost", "Post created successfully");
                    descriptionField.setText("");
                } else {
                    Log.e("CreatePost", "Failed to create post. HTTP Status Code: " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("CreatePost", "Error Body: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("CreatePost", "Error reading error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(fileUri);
            byte[] fileBytes = readBytesFromInputStream(inputStream);

            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileBytes);
            return MultipartBody.Part.createFormData(partName, "file", requestBody);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] readBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData(); // Pobierz URI wybranego zdjęcia
            if (selectedImageUri != null) {
                ImageView imageView = getView().findViewById(R.id.selectedImageView); // Dodaj podgląd
                imageView.setImageURI(selectedImageUri);
            }
        }
        if (requestCode == AUDIO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            recordedAudioUri = data.getData();
            Toast.makeText(requireContext(), "Audio recorded successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void startAudioRecording() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE_PERMISSION);
        } else {
            try {
                // Create a file to save the audio
                File outputDir = requireContext().getCacheDir(); // Use cache directory
                audioFile = File.createTempFile("audio_record", ".3gp", outputDir);

                // Initialize MediaRecorder
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.setOutputFile(audioFile.getAbsolutePath());

                // Start recording
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(requireContext(), "Recording started", Toast.LENGTH_SHORT).show();
                stopRecordingButton.setVisibility(View.VISIBLE);
                recordVoiceButton.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to start recording", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void playRecordedAudio() {
        if (recordedAudioUri != null) {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(recordedAudioUri.getPath());
                    mediaPlayer.prepare();
                }
                mediaPlayer.start();
                Toast.makeText(requireContext(), "Playing audio", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to play audio", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No audio recorded", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_MICROPHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Uprawnienia zostały nadane
                launchAudioRecorder();
            } else {
                // Uprawnienia zostały odrzucone
                Toast.makeText(requireContext(), "Microphone permission is required to record audio", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void launchAudioRecorder() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(intent, AUDIO_REQUEST_CODE);
        } else {
            Toast.makeText(requireContext(), "No app available to record audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAudioRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;

            // Save the recorded audio URI
            recordedAudioUri = Uri.fromFile(audioFile);
            Toast.makeText(requireContext(), "Recording saved: " + audioFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            // Update button visibility
            stopRecordingButton.setVisibility(View.GONE);
            recordVoiceButton.setVisibility(View.VISIBLE);
        }
    }
}
