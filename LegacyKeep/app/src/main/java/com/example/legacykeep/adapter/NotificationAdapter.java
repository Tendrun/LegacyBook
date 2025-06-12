package com.example.legacykeep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legacykeep.R;

import java.util.List;
/**
 * Adapter do wyświetlania listy powiadomień w RecyclerView.
 * Każdy element listy to pojedynczy tekst powiadomienia.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    // Lista tekstów powiadomień
    private List<String> notifications;

    /**
     * Konstruktor adaptera.
     * @param notifications lista powiadomień do wyświetlenia
     */
    public NotificationAdapter(List<String> notifications) {
        this.notifications = notifications;
    }

    /**
     * Aktualizuje listę powiadomień i odświeża widok.
     * @param newNotifications nowa lista powiadomień
     */
    public void updateList(List<String> newNotifications) {
        this.notifications = newNotifications;
        notifyDataSetChanged(); // Informuje RecyclerView o zmianach danych
    }

    /**
     * Tworzy nowy ViewHolder dla elementu powiadomienia.
     */
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflatuje layout XML reprezentujący pojedyncze powiadomienie
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    /**
     * Wypełnia dane w ViewHolderze dla danej pozycji.
     */
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        // Ustawia tekst powiadomienia w polu TextView
        holder.notificationText.setText(notifications.get(position));
    }

    /**
     * Zwraca ilość elementów (powiadomień) w liście.
     */
    @Override
    public int getItemCount() {
        return notifications.size();
    }

    /**
     * ViewHolder odpowiadający za trzymanie referencji do widoku pojedynczego powiadomienia.
     */
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView notificationText;

        /**
         * Konstruktor ViewHoldera, wiążący TextView z layoutem.
         */
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            // Znajduje TextView w layoutcie item_notification.xml
            notificationText = itemView.findViewById(R.id.notificationText);
        }
    }
}
