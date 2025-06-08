// app/src/main/java/com/example/legacykeep/fragments/FeedFragment.java
package com.example.legacykeep.fragments;

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
import com.example.legacykeep.viewmodel.SharedPostViewModel;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private SharedPostViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Pobranie Shared ViewModel z zakresu Activity
        viewModel = new ViewModelProvider(requireActivity())
                .get(SharedPostViewModel.class);

        // Adapter z początkowo pustą listą
        postAdapter = new PostAdapter(new ArrayList<>());
        recyclerView.setAdapter(postAdapter);

        // Obserwacja zmian listy postów
        viewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            postAdapter.updateList(posts);
            recyclerView.scrollToPosition(0);
        });
    }
}
