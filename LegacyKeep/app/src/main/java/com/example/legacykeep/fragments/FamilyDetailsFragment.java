package com.example.legacykeep.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.DTO.FamilyGroup;
import com.example.legacykeep.DTO.UserGroupMembership;
import com.example.legacykeep.R;
import com.example.legacykeep.adapter.FamilyMemberAdapter;
import com.example.legacykeep.model.FamilyMemberModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamilyDetailsFragment extends Fragment {

    private static final String ARG_FAMILY_GROUP_ID = "familyGroupId";
    private long familyGroupId;
    private RecyclerView membersRecyclerView;
    private FamilyMemberAdapter adapter;
    private List<FamilyMemberModel> membersList;
    private Button addMemberButton;

    public static FamilyDetailsFragment newInstance(long familyGroupId) {
        FamilyDetailsFragment fragment = new FamilyDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_FAMILY_GROUP_ID, familyGroupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            familyGroupId = getArguments().getLong(ARG_FAMILY_GROUP_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_family_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        membersRecyclerView = view.findViewById(R.id.familyMembersRecyclerView);
        addMemberButton = view.findViewById(R.id.addMemberButton);

        membersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        membersList = new ArrayList<>();
        adapter = new FamilyMemberAdapter(requireContext(), membersList);
        membersRecyclerView.setAdapter(adapter);

        fetchFamilyMembers();

        addMemberButton.setOnClickListener(v -> {
            // Handle adding a new member (e.g., open a dialog or navigate to another fragment)
            Toast.makeText(requireContext(), "Add Member clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchFamilyMembers() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to view family members", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        Call<FamilyGroup> call = apiService.getFamilyGroupDetails("Bearer " + authToken, familyGroupId);

        call.enqueue(new Callback<FamilyGroup>() {
            @Override
            public void onResponse(Call<FamilyGroup> call, Response<FamilyGroup> response) {
                if (response.isSuccessful() && response.body() != null) {
                    membersList.clear();
                    for (UserGroupMembership membership : response.body().getMemberships()) {
                        membersList.add(new FamilyMemberModel(
                                membership.getUserName(), // Use userName instead of userEmail
                                membership.getRole()
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch family members", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FamilyGroup> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}