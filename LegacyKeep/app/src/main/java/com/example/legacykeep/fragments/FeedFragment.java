package com.example.legacykeep.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legacykeep.R;
import com.example.legacykeep.adapter.PostAdapter;
import com.example.legacykeep.model.PostModel;
import com.example.legacykeep.viewmodel.SharedPostViewModel;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private SharedPostViewModel sharedPostViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    // Android: FeedFragment.java
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize PostAdapter with an empty list
        postAdapter = new PostAdapter(new ArrayList<>());
        recyclerView.setAdapter(postAdapter);

        // Initialize ViewModel
        sharedPostViewModel = new ViewModelProvider(requireActivity()).get(SharedPostViewModel.class);

        // Fetch posts
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);
        if (authToken != null) {
            sharedPostViewModel.fetchPosts(authToken);
        }

        // Observe posts LiveData and update the adapter
        sharedPostViewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            if (posts != null) {
                postAdapter.updateList(posts);
            }
        });
    }
}