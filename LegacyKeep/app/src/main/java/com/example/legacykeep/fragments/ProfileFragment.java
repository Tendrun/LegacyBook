package com.example.legacykeep.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.DTO.UserProfileDTO;
import com.example.legacykeep.R;
import com.example.legacykeep.WelcomeActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final int STORAGE_PERMISSION_REQUEST = 2;
    private static final int CAMERA_PERMISSION_REQUEST = 3;
    private TextView profileName;
    private TextView profileEmail;

    private ImageView profileImage;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        TextView editButton = view.findViewById(R.id.editButton);
        LinearLayout logoutButton = view.findViewById(R.id.logoutRow);


        // Retrieve user data from SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Unknown User");
        String email = sharedPreferences.getString("email", "Unknown Email");

        profileName.setText(username);
        profileEmail.setText(email);

        // Handle logout button click
        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(requireContext(), WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        });

        // Initialize the ActivityResultLaunchers
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            // Load the selected image into the profileImage using Glide
                            Glide.with(requireContext())
                                    .load(selectedImageUri)
                                    .into(profileImage);
                        }
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            Uri imageUri = getImageUriFromBitmap(requireContext(), imageBitmap);
                            if (imageUri != null) {
                                uploadProfilePicture(imageUri);
                            } else {
                                Toast.makeText(requireContext(), "Failed to capture image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

        // Set click listener for the profile image
        profileImage.setOnClickListener(v -> showImageSourceOptions());
        fetchUserProfile();
        return view;
    }

    private Uri getImageUriFromBitmap(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "CapturedImage", null);
        return Uri.parse(path);
    }

    private void showImageSourceOptions() {
        // Show options to choose between camera and gallery
        String[] options = {"Camera", "Gallery"};
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Select Image Source")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Camera option selected
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(requireActivity(),
                                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                        } else {
                            openCamera();
                        }
                    } else if (which == 1) {
                        // Gallery option selected
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(requireActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
                        } else {
                            openGallery();
                        }
                    }
                })
                .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }

    private void uploadProfilePicture(Uri imageUri) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to update your profile picture", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Read the InputStream into a byte array
            byte[] imageData = readBytesFromInputStream(requireContext().getContentResolver().openInputStream(imageUri));

            // Create RequestBody
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageData);
            MultipartBody.Part body = MultipartBody.Part.createFormData("profilePicture", "profile.jpg", requestBody);

            ApiService apiService = ApiClient.getApiService();
            Call<String> call = apiService.updateProfilePicture("Bearer " + authToken, body);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Profile picture updated successfully", Toast.LENGTH_SHORT).show();
                        fetchUserProfile(); // Refresh the profile data
                    } else {
                        Toast.makeText(requireContext(), "Failed to update profile picture", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to read image data", Toast.LENGTH_SHORT).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(requireContext(), "Permission denied to access storage", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(requireContext(), "Permission denied to access camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchUserProfile() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to view your profile", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        Call<UserProfileDTO> call = apiService.getUserProfile("Bearer " + authToken);

        call.enqueue(new Callback<UserProfileDTO>() {
            @Override
            public void onResponse(Call<UserProfileDTO> call, Response<UserProfileDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfileDTO userProfile = response.body();
                    profileName.setText(userProfile.getName());
                    profileEmail.setText(userProfile.getEmail());
                    if (userProfile.getProfilePicture() != null) {
                        Glide.with(requireContext())
                                .load(userProfile.getProfilePicture())
                                .placeholder(R.drawable.ic_profile)
                                .error(R.drawable.ic_profile)
                                .skipMemoryCache(true) // Skip memory cache
                                .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip disk cache
                                .into(profileImage);
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileDTO> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}