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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.DTO.UserProfileDTO;
import com.example.legacykeep.LocaleHelper;
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
    private static final int CAMERA_PERMISSION_REQUEST   = 3;

    private TextView  profileName;
    private TextView  profileEmail;
    private ImageView profileImage;
    private Switch    darkModeSwitch;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    /* ───────────────────────── onCreateView ───────────────────────── */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage  = view.findViewById(R.id.profileImage);
        profileName   = view.findViewById(R.id.profileName);
        profileEmail  = view.findViewById(R.id.profileEmail);
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);

        LinearLayout logoutRow   = view.findViewById(R.id.logoutRow);
        LinearLayout languageRow = view.findViewById(R.id.languageRow);

        SharedPreferences prefs = requireContext()
                .getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);

        profileName.setText(prefs.getString("username", "Unknown User"));
        profileEmail.setText(prefs.getString("email", "Unknown Email"));

        /* ——— Wylogowanie ——— */
        logoutRow.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Intent i = new Intent(requireContext(), WelcomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        });

        /* ——— Zmiana języka ——— */
        languageRow.setOnClickListener(v -> {
            final String[] langs  = {"English", "Polski"};
            final String[] codes  = {"en",      "pl"};

            new android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Select Language")
                    .setItems(langs, (d, which) -> {
                        prefs.edit().putString("app_language", codes[which]).apply();
                        LocaleHelper.setLocale(requireContext(), codes[which]);
                        requireActivity().recreate();
                    })
                    .show();
        });

        /* ——— Dark-mode switch ——— */
        boolean darkEnabled = prefs.getBoolean("dark_mode", false);
        darkModeSwitch.setChecked(darkEnabled);
        darkModeSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        /* ——— Pickery zdjęć ——— */
        initPickers();
        profileImage.setOnClickListener(v -> showImageSourceOptions());

        fetchUserProfile();
        return view;
    }

    /* ───────────────────────── pickery ───────────────────────── */
    private void initPickers() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                res -> {
                    if (res.getResultCode() == AppCompatActivity.RESULT_OK && res.getData() != null) {
                        Uri uri = res.getData().getData();
                        if (uri != null) Glide.with(this).load(uri).into(profileImage);
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                res -> {
                    if (res.getResultCode() == AppCompatActivity.RESULT_OK && res.getData() != null) {
                        Bitmap bmp = (Bitmap) res.getData().getExtras().get("data");
                        Uri uri = getImageUriFromBitmap(requireContext(), bmp);
                        if (uri != null) uploadProfilePicture(uri);
                        else Toast.makeText(requireContext(),
                                "Failed to capture image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showImageSourceOptions() {
        String[] options = {"Camera", "Gallery"};
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Select Image Source")
                .setItems(options, (d, which) -> {
                    if (which == 0) {     // Camera
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(requireActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    CAMERA_PERMISSION_REQUEST);
                        } else openCamera();
                    } else {              // Gallery
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(requireActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    STORAGE_PERMISSION_REQUEST);
                        } else openGallery();
                    }
                }).show();
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(i);
    }

    private void openCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(i);
    }

    private Uri getImageUriFromBitmap(Context ctx, Bitmap bmp) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
        String path = MediaStore.Images.Media.insertImage(
                ctx.getContentResolver(), bmp, "CapturedImage", null);
        return Uri.parse(path);
    }

    /* ───────────────────────── upload zdjęcia ───────────────────────── */
    private void uploadProfilePicture(Uri uri) {
        SharedPreferences prefs = requireContext()
                .getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("authToken", null);
        if (token == null) {
            Toast.makeText(requireContext(),
                    "You must be logged in to update your profile picture",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            byte[] data = readBytesFromInputStream(
                    requireContext().getContentResolver().openInputStream(uri));

            RequestBody reqBody = RequestBody.create(MediaType.parse("image/*"), data);
            MultipartBody.Part part =
                    MultipartBody.Part.createFormData("profilePicture", "profile.jpg", reqBody);

            ApiService api = ApiClient.getApiService();
            api.updateProfilePicture("Bearer " + token, part)
                    .enqueue(new Callback<String>() {
                        @Override public void onResponse(Call<String> c, Response<String> r) {
                            if (r.isSuccessful()) {
                                Toast.makeText(requireContext(),
                                        "Profile picture updated successfully",
                                        Toast.LENGTH_SHORT).show();
                                fetchUserProfile();
                            } else {
                                Toast.makeText(requireContext(),
                                        "Failed to update profile picture",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(Call<String> c, Throwable t) {
                            Toast.makeText(requireContext(),
                                    "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (IOException e) {
            Toast.makeText(requireContext(),
                    "Failed to read image data", Toast.LENGTH_SHORT).show();
        }
    }

    /* ───────────────────────── helper I/O ───────────────────────── */
    private byte[] readBytesFromInputStream(InputStream in) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] tmp = new byte[1024];
        int len;
        while ((len = in.read(tmp)) != -1) buf.write(tmp, 0, len);
        return buf.toByteArray();
    }

    /* ───────────────────────── permissions ───────────────────────── */
    @Override
    public void onRequestPermissionsResult(int req,
                                           @NonNull String[] perms,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(req, perms, grantResults);

        if (req == STORAGE_PERMISSION_REQUEST &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else if (req == CAMERA_PERMISSION_REQUEST &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            Toast.makeText(requireContext(),
                    "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    /* ───────────────────────── pobieranie profilu ───────────────────────── */
    private void fetchUserProfile() {
        SharedPreferences prefs = requireContext()
                .getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("authToken", null);
        if (token == null) {
            Toast.makeText(requireContext(),
                    "You must be logged in to view your profile",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getApiService();
        api.getUserProfile("Bearer " + token)
                .enqueue(new Callback<UserProfileDTO>() {
                    @Override public void onResponse(Call<UserProfileDTO> c,
                                                     Response<UserProfileDTO> r) {
                        if (!isAdded()) return;
                        if (r.isSuccessful() && r.body() != null) {
                            UserProfileDTO u = r.body();
                            profileName.setText(u.getName());
                            profileEmail.setText(u.getEmail());
                            if (u.getProfilePicture() != null) {
                                Glide.with(requireContext())
                                        .load(u.getProfilePicture())
                                        .placeholder(R.drawable.ic_profile)
                                        .error(R.drawable.ic_profile)
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .into(profileImage);
                            }
                        } else {
                            Toast.makeText(requireContext(),
                                    "Failed to fetch profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onFailure(Call<UserProfileDTO> c, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(requireContext(),
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
