package com.example.legacykeep.adapter;

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

    public interface OnFamilyGroupClickListener {
        void onFamilyGroupClick(FamilyGroup familyGroup);
    }

    public FamilyGroupAdapter(Context context, List<FamilyGroup> familyGroups, OnFamilyGroupClickListener listener) {
        this.context = context;
        this.familyGroups = familyGroups;
        this.listener = listener;
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
        holder.itemView.setOnClickListener(v -> listener.onFamilyGroupClick(familyGroup));
    }

    @Override
    public int getItemCount() {
        return familyGroups.size();
    }
}