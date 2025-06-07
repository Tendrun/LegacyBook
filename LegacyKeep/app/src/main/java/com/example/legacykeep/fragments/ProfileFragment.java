package com.example.legacykeep.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.legacykeep.R;
import com.example.legacykeep.WelcomeActivity;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Find the logout row
        LinearLayout logoutRow = view.findViewById(R.id.logoutRow);

        // Set click listener for logout
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