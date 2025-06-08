package com.example.legacykeep.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.DTO.CreateGroupRequest;
import com.example.legacykeep.DTO.FamilyGroup;
import com.example.legacykeep.R;
import com.example.legacykeep.adapter.FamilyMemberAdapter;
import com.example.legacykeep.model.FamilyMemberModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamilyGroupFragment extends Fragment {

    private RecyclerView recyclerView;
    private FamilyMemberAdapter adapter;
    private List<FamilyMemberModel> memberList;

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

        memberList = new ArrayList<>();
        adapter = new FamilyMemberAdapter(requireContext(), memberList);
        recyclerView.setAdapter(adapter);
        fetchUserFamilies();
        // Handle create group button
        view.findViewById(R.id.createFamilyGroupButton).setOnClickListener(v -> showCreateGroupDialog());
    }

    private void showCreateGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Create Family Group");

        // Input field for group name
        final EditText input = new EditText(requireContext());
        input.setHint("Enter family group name");
        builder.setView(input);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String familyName = input.getText().toString().trim();
            if (!familyName.isEmpty()) {
                createFamilyGroup(familyName);
            } else {
                Toast.makeText(requireContext(), "Group name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void createFamilyGroup(String familyName) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to create a group", Toast.LENGTH_SHORT).show();
            return;
        }

        CreateGroupRequest request = new CreateGroupRequest();
        request.setFamilyName(familyName);

        ApiService apiService = ApiClient.getApiService();
        Call<ResponseBody> call = apiService.createFamilyGroup("Bearer " + authToken, request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        System.out.println("Response: " + responseBody);
                        Toast.makeText(requireContext(), "Family group created successfully", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to create family group", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                    memberList.clear();
                    for (FamilyGroup group : response.body()) {
                        memberList.add(new FamilyMemberModel(group.getFamilyName(), "Member")); // Adjust role as needed
                    }
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
}