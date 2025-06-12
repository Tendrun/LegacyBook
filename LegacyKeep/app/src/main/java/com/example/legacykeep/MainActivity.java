package com.example.legacykeep;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.legacykeep.fragments.CreatePostFragment;
import com.example.legacykeep.fragments.FamilyGroupFragment;
import com.example.legacykeep.fragments.FeedFragment;
import com.example.legacykeep.fragments.NotificationsFragment;
import com.example.legacykeep.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

/**
 * Główna aktywność aplikacji LegacyKeep.
 * Zarządza nawigacją pomiędzy fragmentami oraz ustawieniami aplikacji (np. tryb ciemny, uprawnienia Bluetooth).
 */
public class MainActivity extends AppCompatActivity {
    /** Kod żądania uprawnień Bluetooth. */
    private static final int REQUEST_BLUETOOTH_CONNECT = 100;

    /** Widok dolnej nawigacji. */
    private BottomNavigationView bottomNavigationView;

    /**
     * Ustawia lokalizację aplikacji przed załadowaniem zasobów.
     * @param newBase Nowy kontekst bazowy.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }

    /**
     * Metoda wywoływana przy tworzeniu aktywności.
     * Ustawia tryb ciemny, sprawdza uprawnienia Bluetooth oraz inicjalizuje nawigację.
     * @param savedInstanceState Stan zapisany przy poprzednim uruchomieniu (jeśli istnieje).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Ustawienie trybu ciemnego na podstawie preferencji użytkownika
        SharedPreferences prefs = getSharedPreferences("LegacyKeepPrefs", MODE_PRIVATE);
        boolean darkEnabled = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                darkEnabled ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBluetoothPermission();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Załaduj domyślny fragment (Feed)
        loadFragment(new FeedFragment());

        // Obsługa wyboru elementów dolnej nawigacji
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selected;

            int id = item.getItemId();
            if (id == R.id.nav_create) {
                selected = new CreatePostFragment();
            } else if (id == R.id.nav_family) {
                selected = new FamilyGroupFragment();
            } else if (id == R.id.nav_profile) {
                selected = new ProfileFragment();
            } else if (id == R.id.nav_notifications) {
                selected = new NotificationsFragment();
            } else { // R.id.nav_feed i fallback
                selected = new FeedFragment();
            }

            loadFragment(selected);
            return true;
        });
    }

    /**
     * Wczytuje podany fragment do kontenera fragmentów.
     * @param fragment Fragment do wyświetlenia.
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    /**
     * Sprawdza i żąda uprawnień Bluetooth (Android 12+).
     */
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT);
            }
        }
    }

    /**
     * Obsługuje wynik żądania uprawnień.
     * Wyświetla komunikat w zależności od decyzji użytkownika.
     * @param requestCode Kod żądania.
     * @param permissions Tablica żądanych uprawnień.
     * @param grantResults Tablica wyników przyznania uprawnień.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_CONNECT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Uprawnienia Bluetooth przyznane", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Brak uprawnień Bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
    }
}