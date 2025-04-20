package com.example.legacykeep;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.legacykeep.fragments.CreatePostFragment;
import com.example.legacykeep.fragments.FamilyGroupFragment;
import com.example.legacykeep.fragments.FeedFragment;
import com.example.legacykeep.fragments.NotificationsFragment;
import com.example.legacykeep.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load default fragment
        loadFragment(new FeedFragment());

        // Handle navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selected;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_feed) {
                selected = new FeedFragment();
            } else if (itemId == R.id.nav_create) {
                selected = new CreatePostFragment();
            } else if (itemId == R.id.nav_family) {
                selected = new FamilyGroupFragment();
            } else if (itemId == R.id.nav_profile) {
                selected = new ProfileFragment();
            } else if (itemId == R.id.nav_notifications) {
                selected = new NotificationsFragment();
            } else {
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
