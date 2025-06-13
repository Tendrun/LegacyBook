package com.example.legacykeep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.DTO.AuthResponse;
import com.example.legacykeep.DTO.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Aktywność odpowiedzialna za logowanie użytkownika do aplikacji.
 * Umożliwia wprowadzenie adresu e-mail, hasła oraz weryfikację reCAPTCHA.
 * W przypadku poprawnych danych loguje użytkownika i zapisuje token autoryzacyjny.
 */
public class LoginActivity extends AppCompatActivity {

    /** Pole do wprowadzenia adresu e-mail */
    private EditText emailInput;
    /** Pole do wprowadzenia hasła */
    private EditText passwordInput;
    /** Przycisk do zatwierdzenia logowania */
    private Button loginSubmitButton;
    /** Tekst umożliwiający przejście do odzyskiwania hasła */
    private TextView forgotPassword;
    /** Checkbox do potwierdzenia reCAPTCHA */
    private CheckBox recaptchaCheckbox;

    /**
     * Metoda wywoływana przy tworzeniu aktywności.
     * Inicjalizuje widoki i ustawia nasłuchiwanie zdarzeń.
     *
     * @param savedInstanceState zapisany stan aktywności
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicjalizacja widoków
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginSubmitButton = findViewById(R.id.loginSubmitButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        recaptchaCheckbox = findViewById(R.id.recaptchaCheckbox);

        // Obsługa kliknięcia przycisku logowania
        loginSubmitButton.setOnClickListener(v -> loginUser());

        // Obsługa kliknięcia "Zapomniałeś hasła?"
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Metoda obsługująca proces logowania użytkownika.
     * Sprawdza poprawność danych, weryfikuje reCAPTCHA i wysyła żądanie logowania do API.
     * W przypadku sukcesu zapisuje dane użytkownika i przechodzi do głównej aktywności.
     */
    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!recaptchaCheckbox.isChecked()) {
            Toast.makeText(this, "Please verify the reCAPTCHA", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        // Logowanie danych wejściowych (do debugowania)
        System.out.println("LoginRequest email: " + request.getEmail());
        System.out.println("LoginRequest password: " + request.getPassword());

        ApiService apiService = ApiClient.getApiService();
        Call<AuthResponse> call = apiService.login(request);

        call.enqueue(new Callback<AuthResponse>() {
            /**
             * Obsługa odpowiedzi z serwera po próbie logowania.
             *
             * @param call wywołanie Retrofit
             * @param response odpowiedź z serwera
             */
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                // Logowanie odpowiedzi serwera (do debugowania)
                System.out.println("Response code: " + response.code());
                System.out.println("Response message: " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", response.body().getUsername());
                    editor.putString("email", request.getEmail());
                    editor.putString("authToken", response.body().getToken());
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    if (response.errorBody() != null) {
                        try {
                            System.out.println("Error body: " + response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(LoginActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * Obsługa błędu połączenia z serwerem.
             *
             * @param call wywołanie Retrofit
             * @param t wyjątek opisujący błąd
             */
            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}