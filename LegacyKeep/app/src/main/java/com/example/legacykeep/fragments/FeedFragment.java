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
import com.example.legacykeep.adapter.PostAdapter;
import com.example.legacykeep.model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<PostModel> postList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        loadMockPosts();

        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);
    }

    private void loadMockPosts() {
        postList.add(new PostModel(R.drawable.logo_legacykeep, "Grandpa planting the tree", "Kraków"));
        postList.add(new PostModel(R.drawable.logo_legacykeep, "Family picnic 2001", "Warszawa"));
        postList.add(new PostModel(R.drawable.logo_legacykeep, "Sailing trip with dad", "Mazury"));
    }
}
