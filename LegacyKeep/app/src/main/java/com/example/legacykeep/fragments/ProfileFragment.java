package com.example.legacykeep.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.legacykeep.R;
import com.example.legacykeep.WelcomeActivity;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Retrieve username and email from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");
        String email = sharedPreferences.getString("email", "No email available");

        // Set username and email in TextViews
        TextView profileName = view.findViewById(R.id.profileName);
        TextView profileEmail = view.findViewById(R.id.profileEmail);
        profileName.setText(username);
        profileEmail.setText(email);

        // Handle logout
        LinearLayout logoutRow = view.findViewById(R.id.logoutRow);
        logoutRow.setOnClickListener(v -> logout());

        return view;
    }

    private void logout() {
        // Clear authToken from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("authToken");
        editor.apply();

        // Redirect to WelcomeActivity
        Intent intent = new Intent(requireActivity(), WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Finish the current activity
        requireActivity().finish();
    }
}