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

public class FamilyGroupAdapter extends RecyclerView.Adapter<FamilyGroupAdapter.ViewHolder> {

    private final List<FamilyGroup> familyGroups;
    private final Context context;
    private final OnFamilyGroupClickListener listener;
    private final OnDeleteFamilyGroupListener deleteListener;

    public interface OnFamilyGroupClickListener {
        void onFamilyGroupClick(FamilyGroup familyGroup);
    }
    public interface OnDeleteFamilyGroupListener {
        void onDeleteFamilyGroup(FamilyGroup familyGroup);
    }
    public FamilyGroupAdapter(Context context, List<FamilyGroup> familyGroups, OnFamilyGroupClickListener listener, OnDeleteFamilyGroupListener deleteListener) {
        this.context = context;
        this.familyGroups = familyGroups;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView familyNameText;

        public ViewHolder(View view) {
            super(view);
            familyNameText = view.findViewById(R.id.familyGroupName);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_family_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FamilyGroup familyGroup = familyGroups.get(position);
        holder.familyNameText.setText(familyGroup.getFamilyName());

        // Handle click for navigation
        holder.itemView.setOnClickListener(v -> listener.onFamilyGroupClick(familyGroup));

        // Handle long-click for deletion
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

    @Override
    public int getItemCount() {
        return familyGroups.size();
    }
}