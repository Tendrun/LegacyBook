package com.example.legacykeep.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.legacykeep.DTO.DeleteFamilyRequest;
import com.example.legacykeep.DTO.FamilyGroup;
import com.example.legacykeep.R;
import com.example.legacykeep.adapter.FamilyGroupAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
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

        recyclerView = view.findViewById(R.id.familyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        familyGroupList = new ArrayList<>();
        adapter = new FamilyGroupAdapter(requireContext(), familyGroupList, this::onFamilyGroupClick, this::onDeleteFamilyGroup);
        recyclerView.setAdapter(adapter);

        fetchUserFamilies();

        Button createFamilyGroupButton = view.findViewById(R.id.createFamilyGroupButton);
        createFamilyGroupButton.setOnClickListener(v -> showCreateGroupDialog());
    }

    private void showCreateGroupDialog() {
        // Tworzenie AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Create Family Group");

        // Dodanie pola tekstowego do dialogu
        final EditText input = new EditText(requireContext());
        input.setHint("Enter family group name");
        builder.setView(input);

        // Obsługa przycisku "Create"
        builder.setPositiveButton("Create", (dialog, which) -> {
            String familyName = input.getText().toString().trim();
            if (!familyName.isEmpty()) {
                createFamilyGroup(familyName);
            } else {
                Toast.makeText(requireContext(), "Family name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        // Obsługa przycisku "Cancel"
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Wyświetlenie dialogu
        builder.show();
    }

    private void createFamilyGroup(String familyName) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to create a family group", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        CreateGroupRequest request = new CreateGroupRequest();
        request.setFamilyName(familyName);

        Call<ResponseBody> call = apiService.createFamilyGroup("Bearer " + authToken, request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Family group created successfully", Toast.LENGTH_SHORT).show();
                    fetchUserFamilies(); // Odśwież listę grup
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

    private void onDeleteFamilyGroup(FamilyGroup familyGroup) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to delete a family group", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        DeleteFamilyRequest request = new DeleteFamilyRequest();
        request.setGroupId(familyGroup.getId());

        Call<String> call = apiService.deleteFamily("Bearer " + authToken, request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Family group deleted successfully", Toast.LENGTH_SHORT).show();
                    fetchUserFamilies(); // Refresh the list
                } else {
                    Toast.makeText(requireContext(), "Failed to delete family group", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}