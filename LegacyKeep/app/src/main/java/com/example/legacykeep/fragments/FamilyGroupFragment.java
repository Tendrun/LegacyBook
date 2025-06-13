package com.example.legacykeep.fragments;

import android.app.AlertDialog;
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
import com.example.legacykeep.DTO.CreateGroupRequest;
import com.example.legacykeep.DTO.DeleteFamilyRequest;
import com.example.legacykeep.DTO.FamilyGroup;
import com.example.legacykeep.R;
import com.example.legacykeep.adapter.FamilyGroupAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment odpowiedzialny za wyświetlanie, tworzenie i usuwanie grup rodzinnych użytkownika.
 */
public class FamilyGroupFragment extends Fragment {

    /** RecyclerView do wyświetlania listy grup rodzinnych. */
    private RecyclerView recyclerView;
    /** Adapter do obsługi RecyclerView. */
    private FamilyGroupAdapter adapter;
    /** Lista grup rodzinnych użytkownika. */
    private List<FamilyGroup> familyGroupList;

    /**
     * Tworzy widok fragmentu.
     *
     * @param inflater  Inflater do tworzenia widoku.
     * @param container Kontener widoku.
     * @param savedInstanceState Zapisany stan.
     * @return Widok fragmentu.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_family_group, container, false);
    }

    /**
     * Inicjalizuje komponenty po utworzeniu widoku.
     *
     * @param view Widok fragmentu.
     * @param savedInstanceState Zapisany stan.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.familyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        familyGroupList = new ArrayList<>();
        adapter = new FamilyGroupAdapter(requireContext(), familyGroupList, this::onFamilyGroupClick, this::onDeleteFamilyGroup);
        recyclerView.setAdapter(adapter);

        fetchUserFamilies();

        Button createFamilyGroupButton = view.findViewById(R.id.createFamilyGroupButton);
        createFamilyGroupButton.setOnClickListener(v -> showCreateGroupDialog());
    }

    /**
     * Wyświetla okno dialogowe umożliwiające użytkownikowi wprowadzenie nazwy nowej grupy rodzinnej.
     *
     * <p>Dialog zawiera pole tekstowe do wpisania nazwy grupy oraz przyciski "Create" i "Cancel".
     * Po naciśnięciu "Create" i jeśli pole nie jest puste, wywołuje metodę {@link #createFamilyGroup(String)}
     * przekazując wprowadzaną nazwę. W przeciwnym wypadku wyświetla komunikat o błędzie.
     *
     * <p>Przycisk "Cancel" zamyka dialog bez podejmowania żadnej akcji.
     */
    private void showCreateGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Create Family Group");

        final EditText input = new EditText(requireContext());
        input.setHint("Enter family group name");
        builder.setView(input);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String familyName = input.getText().toString().trim();
            if (!familyName.isEmpty()) {
                createFamilyGroup(familyName);
            } else {
                Toast.makeText(requireContext(), "Family name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    /**
     * Tworzy nową grupę rodzinną o podanej nazwie.
     *
     * <p>Metoda odczytuje token uwierzytelniający z SharedPreferences. Jeśli użytkownik nie jest zalogowany,
     * wyświetla komunikat i przerywa działanie.
     *
     * <p>W przeciwnym przypadku wysyła żądanie do API w celu utworzenia grupy rodzinnej z nazwą {@code familyName}.
     * Po pomyślnym utworzeniu wyświetla komunikat potwierdzający oraz odświeża listę grup wywołując {@link #fetchUserFamilies()}.
     * W przypadku błędu wyświetla stosowny komunikat Toast.
     *
     * @param familyName Nazwa nowej grupy rodzinnej do utworzenia.
     */
    private void createFamilyGroup(String familyName) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to create a family group", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        CreateGroupRequest request = new CreateGroupRequest();
        request.setFamilyName(familyName);

        Call<ResponseBody> call = apiService.createFamilyGroup("Bearer " + authToken, request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Family group created successfully", Toast.LENGTH_SHORT).show();
                    fetchUserFamilies();
                } else {
                    Toast.makeText(requireContext(), "Failed to create family group", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Pobiera listę grup rodzinnych powiązanych z zalogowanym użytkownikiem.
     *
     * <p>Metoda odczytuje token uwierzytelniający z SharedPreferences. Jeśli użytkownik
     * nie jest zalogowany, wyświetla komunikat i przerywa działanie.
     *
     * <p>W przeciwnym wypadku wykonuje asynchroniczne wywołanie API, które zwraca listę
     * obiektów {@link FamilyGroup}. Po otrzymaniu danych czyści aktualną listę grup,
     * uzupełnia ją nowymi danymi oraz odświeża adapter wyświetlający listę.
     *
     * <p>W przypadku błędu wyświetla stosowny komunikat Toast.
     */
    private void fetchUserFamilies() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to view family groups", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        Call<List<FamilyGroup>> call = apiService.getUserFamilies("Bearer " + authToken);

        call.enqueue(new Callback<List<FamilyGroup>>() {
            @Override
            public void onResponse(Call<List<FamilyGroup>> call, Response<List<FamilyGroup>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    familyGroupList.clear();
                    familyGroupList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch family groups", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FamilyGroup>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Obsługuje kliknięcie na grupę rodzinną - przechodzi do szczegółów grupy.
     *
     * @param familyGroup Wybrana grupa rodzinna.
     */
    private void onFamilyGroupClick(FamilyGroup familyGroup) {
        FamilyDetailsFragment fragment = FamilyDetailsFragment.newInstance(familyGroup.getId());
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Usuwa wskazaną grupę rodzinną z backendu.
     *
     * <p>Metoda odczytuje token uwierzytelniający z SharedPreferences. Jeśli użytkownik
     * nie jest zalogowany, wyświetla komunikat i przerywa działanie.
     *
     * <p>W przeciwnym wypadku wywołuje asynchronicznie endpoint API odpowiedzialny za usunięcie
     * grupy rodzinnej na podstawie jej unikalnego identyfikatora.
     *
     * <p>Po pomyślnym usunięciu wyświetla komunikat potwierdzający oraz wywołuje metodę {@code fetchUserFamilies()}
     * w celu odświeżenia listy grup użytkownika. W przypadku niepowodzenia wyświetla komunikat o błędzie.
     *
     * @param familyGroup Obiekt {@link FamilyGroup} reprezentujący grupę do usunięcia.
     */
    private void onDeleteFamilyGroup(FamilyGroup familyGroup) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LegacyKeepPrefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(requireContext(), "You must be logged in to delete a family group", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        DeleteFamilyRequest request = new DeleteFamilyRequest();
        request.setGroupId(familyGroup.getId());

        Call<String> call = apiService.deleteFamily("Bearer " + authToken, request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Family group deleted successfully", Toast.LENGTH_SHORT).show();
                    fetchUserFamilies();
                } else {
                    Toast.makeText(requireContext(), "Failed to delete family group", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}