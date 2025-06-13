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

/**
 * Fragment odpowiedzialny za wyświetlanie listy postów w aplikacji.
 * Używa RecyclerView oraz ViewModel do zarządzania i prezentacji danych.
 */
public class FeedFragment extends Fragment {

    /** RecyclerView do wyświetlania listy postów. */
    private RecyclerView recyclerView;
    /** Adapter do obsługi danych postów w RecyclerView. */
    private PostAdapter postAdapter;
    /** ViewModel współdzielony do pobierania i obserwowania postów. */
    private SharedPostViewModel sharedPostViewModel;

    /**
     * Tworzy i zwraca widok hierarchii fragmentu.
     *
     * @param inflater  Obiekt LayoutInflater do "nadmuchania" widoku.
     * @param container Kontener, do którego widok fragmentu zostanie dołączony.
     * @param savedInstanceState Zapisany stan fragmentu (jeśli istnieje).
     * @return Widok fragmentu.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    /**
     * Wywoływana po utworzeniu widoku fragmentu.
     * Inicjalizuje RecyclerView, adapter, ViewModel oraz ustawia obserwatora na dane postów.
     *
     * @param view Widok fragmentu.
     * @param savedInstanceState Zapisany stan fragmentu (jeśli istnieje).
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicjalizacja RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicjalizacja adaptera z pustą listą
        postAdapter = new PostAdapter(new ArrayList<>());
        recyclerView.setAdapter(postAdapter);

        // Inicjalizacja ViewModel
        sharedPostViewModel = new ViewModelProvider(requireActivity()).get(SharedPostViewModel.class);

        // Pobranie tokenu autoryzacyjnego z SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);
        if (authToken != null) {
            sharedPostViewModel.fetchPosts(authToken);
        }

        // Obserwacja LiveData z postami i aktualizacja adaptera
        sharedPostViewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            if (posts != null) {
                postAdapter.updateList(posts);
                sharedPostViewModel.updatePostCount(posts.size());
            }
        });
    }
}