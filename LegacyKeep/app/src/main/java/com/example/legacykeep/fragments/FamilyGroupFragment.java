package com.example.legacykeep.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legacykeep.R;
import com.example.legacykeep.adapter.FamilyMemberAdapter;
import com.example.legacykeep.model.FamilyMemberModel;

import java.util.ArrayList;
import java.util.List;

public class FamilyGroupFragment extends Fragment {

    private RecyclerView recyclerView;
    private FamilyMemberAdapter adapter;
    private List<FamilyMemberModel> memberList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_family_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.familyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        memberList = new ArrayList<>();
        memberList.add(new FamilyMemberModel("Adam", "Grandfather"));
        memberList.add(new FamilyMemberModel("Isabella", "Mother"));
        memberList.add(new FamilyMemberModel("Leo", "Son"));

        adapter = new FamilyMemberAdapter(getContext(), memberList);
        recyclerView.setAdapter(adapter);
    }
}
