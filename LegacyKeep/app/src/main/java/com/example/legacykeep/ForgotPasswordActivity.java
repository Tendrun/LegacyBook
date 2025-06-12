package com.example.legacykeep;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Aktywność odpowiedzialna za obsługę procesu resetowania hasła użytkownika.
 * Pozwala użytkownikowi wprowadzić adres e-mail oraz nowe hasło,
 * a następnie wysyła żądanie resetu do serwera.
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    /** Pole do wprowadzenia adresu e-mail użytkownika. */
    private EditText emailInput;
    /** Przycisk wysyłający żądanie resetu hasła. */
    private Button sendResetButton;
    /** Tekst wyświetlający potwierdzenie zresetowania hasła. */
    private TextView confirmationMessage;

    /**
     * Metoda wywoływana przy tworzeniu aktywności.
     * Inicjalizuje widoki i ustawia obsługę kliknięcia przycisku resetu.
     *
     * @param savedInstanceState zapisany stan aktywności
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.forgotEmailInput);
        EditText newPasswordInput = findViewById(R.id.newPasswordInput);
        sendResetButton = findViewById(R.id.sendResetButton);
        confirmationMessage = findViewById(R.id.resetConfirmationMessage);

        sendResetButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String newPassword = newPasswordInput.getText().toString().trim();

            if (email.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Wszystkie pola są wymagane", Toast.LENGTH_SHORT).show();
                return;
            }

            resetPassword(email, newPassword);
        });
    }

    /**
     * Wysyła żądanie resetu hasła do serwera.
     *
     * @param email adres e-mail użytkownika
     * @param newPassword nowe hasło do ustawienia
     */
    private void resetPassword(String email, String newPassword) {
        ApiService apiService = ApiClient.getApiService();
        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("newPassword", newPassword);

        Call<String> call = apiService.resetPassword(request);
        call.enqueue(new Callback<String>() {
            /**
             * Obsługuje odpowiedź z serwera.
             *
             * @param call wywołanie Retrofit
             * @param response odpowiedź z serwera
             */
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    confirmationMessage.setVisibility(View.VISIBLE);
                    confirmationMessage.setText("Hasło zostało zresetowane!");
                } else {
                    try {
                        int statusCode = response.code();
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Nieznany błąd";
                        System.out.println("Błąd odpowiedzi: " + errorBody);
                        System.out.println("Kod HTTP: " + statusCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ForgotPasswordActivity.this, "Nie udało się zresetować hasła", Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * Obsługuje błąd połączenia z serwerem.
             *
             * @param call wywołanie Retrofit
             * @param t wyjątek opisujący błąd
             */
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Błąd: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}