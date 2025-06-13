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
import com.example.legacykeep.model.PostModel;
import com.example.legacykeep.viewmodel.SharedPostViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment odpowiedzialny za wyświetlanie powiadomień o nowych postach.
 * Obserwuje zmiany w liście postów i aktualizuje widok powiadomień.
 */
public class NotificationsFragment extends Fragment {

    /** Adapter obsługujący wyświetlanie powiadomień w RecyclerView. */
    private NotificationAdapter notificationAdapter;

    /**
     * Tworzy i zwraca widok hierarchii fragmentu.
     *
     * @param inflater  Obiekt do "nadmuchiwania" layoutu XML.
     * @param container Rodzic, do którego zostanie dołączony widok fragmentu.
     * @param savedInstanceState Poprzedni stan fragmentu, jeśli istnieje.
     * @return Widok fragmentu.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    /**
     * Wywoływana po utworzeniu widoku fragmentu.
     * Inicjalizuje RecyclerView oraz obserwuje zmiany w liście postów,
     * aby aktualizować powiadomienia.
     *
     * @param view Utworzony widok fragmentu.
     * @param savedInstanceState Poprzedni stan fragmentu, jeśli istnieje.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        notificationAdapter = new NotificationAdapter(new ArrayList<>());
        recyclerView.setAdapter(notificationAdapter);

        SharedPostViewModel sharedPostViewModel = new ViewModelProvider(requireActivity()).get(SharedPostViewModel.class);

        // Obserwuje listę postów i aktualizuje powiadomienia
        sharedPostViewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            if (posts != null) {
                List<String> notifications = new ArrayList<>();
                for (PostModel post : posts) {
                    notifications.add("Nowy post od " + post.getAuthorName() + ": " + post.getContent());
                }
                notificationAdapter.updateList(notifications);
            }
        });
    }
}