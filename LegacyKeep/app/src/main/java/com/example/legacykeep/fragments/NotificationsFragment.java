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
import com.example.legacykeep.adapter.NotificationAdapter;
import com.example.legacykeep.viewmodel.SharedPostViewModel;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotificationAdapter notificationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        notificationAdapter = new NotificationAdapter(new ArrayList<>());
        recyclerView.setAdapter(notificationAdapter);

        SharedPostViewModel sharedPostViewModel = new ViewModelProvider(requireActivity()).get(SharedPostViewModel.class);

        sharedPostViewModel.getPostCount().observe(getViewLifecycleOwner(), postCount -> {
            if (postCount != null && postCount > 0) {
                // Example: Add a notification for new posts
                ArrayList<String> notifications = new ArrayList<>();
                notifications.add("New posts available: " + postCount);
                notificationAdapter.updateList(notifications);
            }
        });
    }
}