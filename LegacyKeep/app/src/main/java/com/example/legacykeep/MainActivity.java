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

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_BLUETOOTH_CONNECT = 100;

    private BottomNavigationView bottomNavigationView;

    /** Ustaw lokalizację zanim załadujemy zasoby. */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* ─── Tryb ciemny przed inflacją layoutu ─── */
        SharedPreferences prefs = getSharedPreferences("LegacyKeepPrefs", MODE_PRIVATE);
        boolean darkEnabled = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                darkEnabled ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        checkBluetoothPermission();  // <<< to dodaj tutaj

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        /* Domyślny fragment */
        loadFragment(new FeedFragment());

        /* Obsługa bottom-nav (if/else – brak problemu z const expr) */
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
            } else {                        // R.id.nav_feed i fallback
                selected = new FeedFragment();
            }

            loadFragment(selected);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT);
            }
        }
    }

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
