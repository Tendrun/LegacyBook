package com.example.legacykeep;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.DTO.RegisterRequest;
import com.example.legacykeep.DTO.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private Spinner roleSpinner, languageSpinner;
    private ImageView addPhotoButton;
    private CheckBox recaptchaCheckbox;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        roleSpinner = findViewById(R.id.roleSpinner);
        languageSpinner = findViewById(R.id.languageSpinner);
        addPhotoButton = findViewById(R.id.addPhotoButton);
        recaptchaCheckbox = findViewById(R.id.recaptchaCheckbox);
        registerButton = findViewById(R.id.registerButton);

        // Handle register button click
        registerButton.setOnClickListener(v -> registerUser());
    }
    private boolean isValidEmail(String email) {
        String emailPattern = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
        return email.matches(emailPattern);
    }

    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Safely retrieve selected values from spinners
        String role = roleSpinner.getSelectedItem() != null ? roleSpinner.getSelectedItem().toString() : "";
        String language = languageSpinner.getSelectedItem() != null ? languageSpinner.getSelectedItem().toString() : "";

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!recaptchaCheckbox.isChecked()) {
            Toast.makeText(this, "Please verify the reCAPTCHA", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create request object
        RegisterRequest request = new RegisterRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);

        // Send request to backend
        ApiService apiService = ApiClient.getApiService();
        Call<RegisterResponse> call = apiService.register(request);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(RegisterActivity.this, message != null ? message : "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}