package com.example.legacykeep.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.DTO.FamilyGroup;
import com.example.legacykeep.R;
import com.example.legacykeep.adapter.FamilyGroupAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamilyGroupFragment extends Fragment {

    private RecyclerView recyclerView;
    private FamilyGroupAdapter adapter;
    private List<FamilyGroup> familyGroupList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_family_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.familyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        familyGroupList = new ArrayList<>();
        adapter = new FamilyGroupAdapter(requireContext(), familyGroupList, this::onFamilyGroupClick);
        recyclerView.setAdapter(adapter);

        fetchUserFamilies();
    }

    private void fetchUserFamilies() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to view family groups", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        Call<List<FamilyGroup>> call = apiService.getUserFamilies("Bearer " + authToken);

        call.enqueue(new Callback<List<FamilyGroup>>() {
            @Override
            public void onResponse(Call<List<FamilyGroup>> call, Response<List<FamilyGroup>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    familyGroupList.clear();
                    familyGroupList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch family groups", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FamilyGroup>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onFamilyGroupClick(FamilyGroup familyGroup) {
        // Navigate to FamilyDetailsFragment
        FamilyDetailsFragment fragment = FamilyDetailsFragment.newInstance(familyGroup.getId());
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}