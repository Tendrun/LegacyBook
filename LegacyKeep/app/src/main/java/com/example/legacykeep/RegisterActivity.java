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

/**
 * Aktywność rejestracji użytkownika w aplikacji LegacyKeep.
 * Pozwala użytkownikowi wprowadzić dane i zarejestrować się w systemie.
 */
public class RegisterActivity extends AppCompatActivity {

    // Pola interfejsu użytkownika
    private EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private Spinner roleSpinner, languageSpinner;
    private ImageView addPhotoButton;
    private CheckBox recaptchaCheckbox;
    private Button registerButton;

    /**
     * Metoda uruchamiana podczas tworzenia aktywności.
     *
     * @param savedInstanceState Zapisany stan instancji (jeśli dotyczy).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicjalizacja widoków interfejsu użytkownika
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        roleSpinner = findViewById(R.id.roleSpinner);
        languageSpinner = findViewById(R.id.languageSpinner);
        addPhotoButton = findViewById(R.id.addPhotoButton);
        recaptchaCheckbox = findViewById(R.id.recaptchaCheckbox);
        registerButton = findViewById(R.id.registerButton);

        // Ustawienie obsługi kliknięcia przycisku rejestracji
        registerButton.setOnClickListener(v -> registerUser());
    }

    /**
     * Sprawdza, czy podany adres e-mail jest zgodny z podstawowym wzorcem e-maila.
     *
     * @param email Adres e-mail do sprawdzenia.
     * @return true jeśli e-mail jest poprawny, false w przeciwnym wypadku.
     */
    private boolean isValidEmail(String email) {
        String emailPattern = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
        return email.matches(emailPattern);
    }

    /**
     * Metoda odpowiedzialna za proces rejestracji użytkownika:
     * - Waliduje dane wejściowe.
     * - Tworzy żądanie rejestracyjne.
     * - Wysyła dane do serwera za pomocą Retrofit.
     * - Obsługuje odpowiedź z serwera.
     */
    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Pobranie wartości z rozwijanych list (z obsługą null)
        String role = roleSpinner.getSelectedItem() != null ? roleSpinner.getSelectedItem().toString() : "";
        String language = languageSpinner.getSelectedItem() != null ? languageSpinner.getSelectedItem().toString() : "";

        // Walidacja danych wejściowych
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

        // Utworzenie obiektu żądania
        RegisterRequest request = new RegisterRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);

        // Wysłanie żądania do serwera
        ApiService apiService = ApiClient.getApiService();
        Call<RegisterResponse> call = apiService.register(request);

        // Obsługa odpowiedzi z serwera
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(RegisterActivity.this, message != null ? message : "Registration successful", Toast.LENGTH_SHORT).show();

                    // Przejście do ekranu logowania po udanej rejestracji
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                // Obsługa błędów sieciowych lub innych problemów
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
