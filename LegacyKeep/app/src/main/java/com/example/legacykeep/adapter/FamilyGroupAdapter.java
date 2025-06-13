package com.example.legacykeep.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legacykeep.DTO.FamilyGroup;
import com.example.legacykeep.R;

import java.util.List;

/**
 * Adapter RecyclerView wyświetlający listę grup rodzinnych.
 * Obsługuje kliknięcia na elementy listy oraz długie kliknięcia służące do usuwania grup.
 */
public class FamilyGroupAdapter extends RecyclerView.Adapter<FamilyGroupAdapter.ViewHolder> {

    private final List<FamilyGroup> familyGroups;
    private final Context context;
    private final OnFamilyGroupClickListener listener;
    private final OnDeleteFamilyGroupListener deleteListener;

    /**
     * Interfejs do obsługi kliknięcia na element listy grup rodzinnych.
     */
    public interface OnFamilyGroupClickListener {
        /**
         * Wywoływane po kliknięciu na grupę rodzinną.
         * @param familyGroup kliknięta grupa rodzinna
         */
        void onFamilyGroupClick(FamilyGroup familyGroup);
    }

    /**
     * Interfejs do obsługi usuwania grupy rodzinnej po długim kliknięciu.
     */
    public interface OnDeleteFamilyGroupListener {
        /**
         * Wywoływane po potwierdzeniu usunięcia grupy rodzinnej.
         * @param familyGroup grupa rodzinna do usunięcia
         */
        void onDeleteFamilyGroup(FamilyGroup familyGroup);
    }

    /**
     * Konstruktor adaptera.
     * @param context kontekst aplikacji
     * @param familyGroups lista grup rodzinnych do wyświetlenia
     * @param listener listener obsługujący kliknięcia na grupę
     * @param deleteListener listener obsługujący usuwanie grupy
     */
    public FamilyGroupAdapter(Context context, List<FamilyGroup> familyGroups, OnFamilyGroupClickListener listener, OnDeleteFamilyGroupListener deleteListener) {
        this.context = context;
        this.familyGroups = familyGroups;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    /**
     * ViewHolder reprezentujący pojedynczy element listy.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView familyNameText;

        /**
         * Konstruktor ViewHoldera.
         * @param view widok pojedynczego elementu listy
         */
        public ViewHolder(View view) {
            super(view);
            familyNameText = view.findViewById(R.id.familyGroupName);
        }
    }

    /**
     * Tworzy nowy ViewHolder przez nadmuchanie layoutu elementu listy.
     * @param parent rodzic widoku
     * @param viewType typ widoku (nieużywany)
     * @return nowy ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_family_group, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Wiąże dane grupy rodzinnej z ViewHolderem.
     * Ustawia nazwę grupy oraz obsługę kliknięć i długich kliknięć (usuwanie).
     * @param holder ViewHolder do wypełnienia danymi
     * @param position pozycja elementu na liście
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FamilyGroup familyGroup = familyGroups.get(position);
        holder.familyNameText.setText(familyGroup.getFamilyName());

        // Obsługa kliknięcia - przejście do szczegółów grupy
        holder.itemView.setOnClickListener(v -> listener.onFamilyGroupClick(familyGroup));

        // Obsługa długiego kliknięcia - wyświetlenie dialogu potwierdzenia usunięcia grupy
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Family Group")
                    .setMessage("Are you sure you want to delete this family group?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (deleteListener != null) {
                            deleteListener.onDeleteFamilyGroup(familyGroup);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    /**
     * Zwraca liczbę elementów na liście grup rodzinnych.
     * @return liczba grup rodzinnych
     */
    @Override
    public int getItemCount() {
        return familyGroups.size();
    }
}
