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

public class MainActivity extends AppCompatActivity {

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
}
