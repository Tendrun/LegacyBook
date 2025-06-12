package com.example.legacykeep.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legacykeep.API.ApiClient;
import com.example.legacykeep.API.ApiService;
import com.example.legacykeep.DTO.AddMemberRequest;
import com.example.legacykeep.DTO.DeleteMemberRequest;
import com.example.legacykeep.DTO.FamilyGroup;
import com.example.legacykeep.DTO.UserGroupMembership;
import com.example.legacykeep.R;
import com.example.legacykeep.adapter.FamilyMemberAdapter;
import com.example.legacykeep.model.FamilyMemberModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment odpowiedzialny za wyświetlanie szczegółów grupy rodzinnej,
 * zarządzanie członkami rodziny oraz obsługę dodawania i usuwania członków.
 */
public class FamilyDetailsFragment extends Fragment {

    /** Klucz argumentu identyfikatora grupy rodzinnej */
    private static final String ARG_FAMILY_GROUP_ID = "familyGroupId";
    /** Identyfikator grupy rodzinnej */
    private long familyGroupId;
    /** RecyclerView do wyświetlania członków rodziny */
    private RecyclerView membersRecyclerView;
    /** Adapter do obsługi listy członków */
    private FamilyMemberAdapter adapter;
    /** Lista modeli członków rodziny */
    private List<FamilyMemberModel> membersList;
    /** Przycisk dodawania członka */
    private Button addMemberButton;

    /**
     * Tworzy nową instancję fragmentu z przekazanym identyfikatorem grupy rodzinnej.
     * @param familyGroupId identyfikator grupy rodzinnej
     * @return nowa instancja FamilyDetailsFragment
     */
    public static FamilyDetailsFragment newInstance(long familyGroupId) {
        FamilyDetailsFragment fragment = new FamilyDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_FAMILY_GROUP_ID, familyGroupId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Wywoływana przy tworzeniu fragmentu.
     * @param savedInstanceState zapisany stan fragmentu
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            familyGroupId = getArguments().getLong(ARG_FAMILY_GROUP_ID);
        }
    }

    /**
     * Tworzy i zwraca widok hierarchii fragmentu.
     * @param inflater obiekt do "nadmuchiwania" widoków
     * @param container kontener widoku
     * @param savedInstanceState zapisany stan fragmentu
     * @return widok fragmentu
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_family_details, container, false);
    }

    /**
     * Wywoływana po utworzeniu widoku fragmentu.
     * Inicjalizuje widoki, adapter oraz pobiera listę członków rodziny.
     * @param view widok fragmentu
     * @param savedInstanceState zapisany stan fragmentu
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        membersRecyclerView = view.findViewById(R.id.familyMembersRecyclerView);
        EditText addMemberEmailInput = view.findViewById(R.id.addMemberEmailInput);
        Button addMemberConfirmButton = view.findViewById(R.id.addMemberConfirmButton);

        membersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        membersList = new ArrayList<>();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        adapter = new FamilyMemberAdapter(requireContext(), membersList, this::onDeleteMember, familyGroupId, authToken);
        membersRecyclerView.setAdapter(adapter);

        fetchFamilyMembers();

        addMemberConfirmButton.setOnClickListener(v -> {
            String email = addMemberEmailInput.getText().toString().trim();
            if (!email.isEmpty()) {
                addMemberToFamilyGroup(email);
            } else {
                Toast.makeText(requireContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Dodaje nowego członka do grupy rodzinnej na podstawie podanego adresu e-mail.
     *
     * <p>Metoda pobiera token uwierzytelniający z SharedPreferences. Jeśli użytkownik nie jest zalogowany,
     * wyświetla komunikat i przerywa operację. W przeciwnym razie wywołuje endpoint API, który dodaje
     * użytkownika do grupy rodzinnej o ID {@code familyGroupId}.
     *
     * <p>Po pomyślnym dodaniu członka następuje odświeżenie listy członków poprzez wywołanie
     * {@link #fetchFamilyMembers()}, w przeciwnym wypadku wyświetlany jest komunikat o błędzie.
     *
     * @param email Adres e-mail użytkownika, który ma zostać dodany do grupy rodzinnej.
     */
    private void addMemberToFamilyGroup(String email) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to add members", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        AddMemberRequest request = new AddMemberRequest();
        request.setUserEmailToAdd(email);
        request.setGroupId(familyGroupId);

        Call<String> call = apiService.addMemberToFamilyGroup("Bearer " + authToken, request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Member added successfully", Toast.LENGTH_SHORT).show();
                    fetchFamilyMembers();
                } else {
                    Toast.makeText(requireContext(), "Failed to add member", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Pobiera szczegóły grupy rodzinnej wraz z listą członków z backendu.
     *
     * <p>Metoda odczytuje token uwierzytelniający z SharedPreferences. Jeśli użytkownik
     * nie jest zalogowany, wyświetla odpowiedni komunikat i przerywa działanie.
     *
     * <p>W przeciwnym wypadku wykonuje asynchroniczne wywołanie API, które zwraca
     * obiekt {@link FamilyGroup} zawierający listę członków grupy.
     * Po poprawnym otrzymaniu danych czyści aktualną listę członków i uzupełnia ją nowymi
     * danymi, a następnie odświeża adapter RecyclerView.
     *
     * <p>W przypadku błędu wyświetla odpowiedni komunikat Toast.
     */
    private void fetchFamilyMembers() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to view family members", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        Call<FamilyGroup> call = apiService.getFamilyGroupDetails("Bearer " + authToken, familyGroupId);

        call.enqueue(new Callback<FamilyGroup>() {
            @Override
            public void onResponse(Call<FamilyGroup> call, Response<FamilyGroup> response) {
                if (response.isSuccessful() && response.body() != null) {
                    membersList.clear();
                    for (UserGroupMembership membership : response.body().getMemberships()) {
                        membersList.add(new FamilyMemberModel(
                                membership.getUserEmail(),
                                membership.getRole(),
                                membership.getFamilyRole() != null ? membership.getFamilyRole().name() : "None"
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch family members", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FamilyGroup> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Usuwa wskazanego członka grupy rodzinnej.
     *
     * <p>Metoda pobiera token uwierzytelniający z SharedPreferences. Jeśli użytkownik nie jest zalogowany,
     * wyświetla komunikat i przerywa operację. W przeciwnym wypadku wywołuje endpoint API do usunięcia
     * członka na podstawie jego adresu e-mail i ID grupy rodzinnej.
     *
     * <p>W przypadku powodzenia operacji odświeża listę członków grupy, a w razie błędu wyświetla odpowiedni komunikat.
     *
     * @param member Obiekt FamilyMemberModel reprezentujący członka do usunięcia.
     */
    private void onDeleteMember(FamilyMemberModel member) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to remove a member", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        DeleteMemberRequest request = new DeleteMemberRequest();
        request.setUserEmailToDelete(member.getEmail());
        request.setGroupId(familyGroupId);

        Call<String> call = apiService.deleteMemberFromFamilyGroup("Bearer " + authToken, request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Member removed successfully", Toast.LENGTH_SHORT).show();
                    fetchFamilyMembers();
                } else {
                    Toast.makeText(requireContext(), "Failed to remove member", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}