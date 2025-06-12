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

public class FamilyMemberAdapter extends RecyclerView.Adapter<FamilyMemberAdapter.ViewHolder> {

    private final List<FamilyMemberModel> members;
    private final Context context;
    private final OnDeleteMemberListener deleteListener;
    private final long groupId;
    private final String authToken;

    public FamilyMemberAdapter(Context context, List<FamilyMemberModel> members, OnDeleteMemberListener deleteListener, long groupId, String authToken) {
        this.context = context;
        this.members = members;
        this.deleteListener = deleteListener;
        this.groupId = groupId;
        this.authToken = authToken;
    }

    public interface OnDeleteMemberListener {
        void onDeleteMember(FamilyMemberModel member);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        Spinner familyRoleSpinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.memberName);
            familyRoleSpinner = itemView.findViewById(R.id.memberFamilyRoleSpinner);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_family_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FamilyMemberModel member = members.get(position);
        holder.nameText.setText(member.getEmail());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                context.getResources().getStringArray(R.array.family_roles)
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.familyRoleSpinner.setAdapter(spinnerAdapter);

        int selectedIndex = spinnerAdapter.getPosition(member.getFamilyRole());
        if (selectedIndex >= 0) {
            holder.familyRoleSpinner.setSelection(selectedIndex);
        }

        holder.familyRoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRole = (String) parent.getItemAtPosition(position);
                if (!selectedRole.equals(member.getFamilyRole())) {
                    member.setFamilyRole(selectedRole);

                    SetFamilyRoleRequest request = new SetFamilyRoleRequest();
                    request.setUserEmailRole(member.getEmail());
                    request.setFamilyRole(selectedRole);
                    request.setGroupId(groupId);

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
            }
        });

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

    @Override
    public int getItemCount() {
        return members.size();
    }
}