package com.example.legacykeep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * WelcomeActivity to ekran powitalny aplikacji LegacyKeep.
 * Sprawdza, czy użytkownik jest już zalogowany (na podstawie authToken w SharedPreferences).
 * Jeśli tak, przekierowuje do MainActivity.
 * Jeśli nie, wyświetla przyciski logowania i rejestracji.
 */
public class WelcomeActivity extends AppCompatActivity {

    /**
     * Metoda wywoływana przy tworzeniu aktywności.
     * Sprawdza, czy użytkownik jest zalogowany i odpowiednio przekierowuje lub wyświetla ekran powitalny.
     *
     * @param savedInstanceState stan zapisany aktywności (jeśli istnieje)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sprawdzenie, czy użytkownik jest zalogowany na podstawie authToken w SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken != null) {
            // Użytkownik jest zalogowany, przekierowanie do MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Wyświetlenie ekranu powitalnego z przyciskami logowania i rejestracji
            setContentView(R.layout.activity_welcome);

            Button loginButton = findViewById(R.id.loginButton);
            Button createAccountButton = findViewById(R.id.createAccountButton);

            // Obsługa kliknięcia przycisku logowania
            loginButton.setOnClickListener(v -> openLoginScreen());
            // Obsługa kliknięcia przycisku rejestracji
            createAccountButton.setOnClickListener(v -> openRegisterScreen());
        }
    }

    /**
     * Otwiera ekran logowania (LoginActivity).
     */
    private void openLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Otwiera ekran rejestracji (RegisterActivity).
     */
    private void openRegisterScreen() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}