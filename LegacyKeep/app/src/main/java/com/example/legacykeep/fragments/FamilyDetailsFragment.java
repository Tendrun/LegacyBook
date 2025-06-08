package com.example.legacykeep.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.legacykeep.R;

public class FamilyDetailsFragment extends Fragment {

    private static final String ARG_FAMILY_GROUP_ID = "familyGroupId";
    private long familyGroupId;

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
}