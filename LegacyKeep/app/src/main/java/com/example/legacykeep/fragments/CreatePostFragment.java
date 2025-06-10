// app/src/main/java/com/example/legacykeep/fragments/CreatePostFragment.java
package com.example.legacykeep.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private Uri selectedImageUri = null;
    private Uri selectedAudioUri = null;

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
        submitPostButton = view.findViewById(R.id.buttonSubmitPost);

        // Handle photo upload
        uploadPhotoButton.setOnClickListener(v -> openGallery());

        // Handle audio recording
        recordVoiceButton.setOnClickListener(v -> startAudioRecording());

        // Handle post submission
        submitPostButton.setOnClickListener(v -> submitPost());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void startAudioRecording() {
        // Implement audio recording logic
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
        if (selectedAudioUri != null) {
            audioPart = prepareFilePart("audio", selectedAudioUri);
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
    }
}
