package com.example.legacykeep.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.DTO.SetFamilyRoleRequest;
import com.example.legacykeep.R;
import com.example.legacykeep.model.FamilyMemberModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adapter do wyświetlania i zarządzania członkami rodziny w RecyclerView.
 * Pozwala na zmianę roli członka rodziny oraz usuwanie członków z grupy.
 */
public class FamilyMemberAdapter extends RecyclerView.Adapter<FamilyMemberAdapter.ViewHolder> {

    /** Lista członków rodziny do wyświetlenia. */
    private final List<FamilyMemberModel> members;
    /** Kontekst aplikacji. */
    private final Context context;
    /** Listener obsługujący usuwanie członków. */
    private final OnDeleteMemberListener deleteListener;
    /** Identyfikator grupy rodzinnej. */
    private final long groupId;
    /** Token autoryzacyjny użytkownika. */
    private final String authToken;

    /**
     * Konstruktor adaptera.
     *
     * @param context        kontekst aplikacji
     * @param members        lista członków rodziny
     * @param deleteListener listener obsługujący usuwanie członków
     * @param groupId        identyfikator grupy rodzinnej
     * @param authToken      token autoryzacyjny użytkownika
     */
    public FamilyMemberAdapter(Context context, List<FamilyMemberModel> members, OnDeleteMemberListener deleteListener, long groupId, String authToken) {
        this.context = context;
        this.members = members;
        this.deleteListener = deleteListener;
        this.groupId = groupId;
        this.authToken = authToken;
    }

    /**
     * Interfejs do obsługi usuwania członka rodziny.
     */
    public interface OnDeleteMemberListener {
        /**
         * Wywoływane po potwierdzeniu usunięcia członka rodziny.
         *
         * @param member model członka rodziny do usunięcia
         */
        void onDeleteMember(FamilyMemberModel member);
    }

    /**
     * ViewHolder przechowujący widoki pojedynczego elementu listy.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /** Tekst z adresem e-mail członka rodziny. */
        TextView nameText;
        /** Spinner do wyboru roli rodzinnej. */
        Spinner familyRoleSpinner;

        /**
         * Konstruktor ViewHoldera.
         *
         * @param itemView widok pojedynczego elementu listy
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.memberName);
            familyRoleSpinner = itemView.findViewById(R.id.memberFamilyRoleSpinner);
        }
    }

    /**
     * Tworzy nowy ViewHolder dla elementu listy.
     *
     * @param parent   rodzic ViewGroup
     * @param viewType typ widoku
     * @return nowy ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_family_member, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Wypełnia widok danymi członka rodziny oraz obsługuje interakcje użytkownika.
     *
     * @param holder   ViewHolder do wypełnienia
     * @param position pozycja elementu w liście
     */

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Pobierz członka na podstawie pozycji
        FamilyMemberModel member = members.get(position);

        // Wyświetl email jako nazwę członka
        holder.nameText.setText(member.getEmail());

        // Stwórz adapter dla spinnera z listą dostępnych ról rodzinnych
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                context.getResources().getStringArray(R.array.family_roles)
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.familyRoleSpinner.setAdapter(spinnerAdapter);

        // Ustaw aktualnie przypisaną rolę w spinnerze
        int selectedIndex = spinnerAdapter.getPosition(member.getFamilyRole());
        if (selectedIndex >= 0) {
            holder.familyRoleSpinner.setSelection(selectedIndex);
        }

        // Obsługa zmiany roli w spinnerze
        holder.familyRoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRole = (String) parent.getItemAtPosition(position);

                // Sprawdź, czy rola została zmieniona
                if (!selectedRole.equals(member.getFamilyRole())) {
                    // Zaktualizuj lokalny model
                    member.setFamilyRole(selectedRole);

                    // Przygotuj żądanie do API
                    SetFamilyRoleRequest request = new SetFamilyRoleRequest();
                    request.setUserEmailRole(member.getEmail());
                    request.setFamilyRole(selectedRole);
                    request.setGroupId(groupId);

                    // Wywołanie API do aktualizacji roli
                    ApiService apiService = ApiClient.getApiService();
                    apiService.setFamilyRole("Bearer " + authToken, request).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Role updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Failed to update role", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nie trzeba nic robić
            }
        });

        // Obsługa długiego kliknięcia — pytanie o potwierdzenie usunięcia
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Remove Member")
                    .setMessage("Are you sure you want to remove this member?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (deleteListener != null) {
                            deleteListener.onDeleteMember(member);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    /**
     * Zwraca liczbę elementów w liście członków rodziny.
     *
     * @return liczba członków rodziny
     */
    @Override
    public int getItemCount() {
        return members.size();
    }
}