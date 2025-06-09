package com.example.legacykeep.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legacykeep.R;
import com.example.legacykeep.model.FamilyMemberModel;

import java.util.List;

public class FamilyMemberAdapter extends RecyclerView.Adapter<FamilyMemberAdapter.ViewHolder> {

    private final List<FamilyMemberModel> members;
    private final Context context;
    private final OnDeleteMemberListener deleteListener;

    public FamilyMemberAdapter(Context context, List<FamilyMemberModel> members, OnDeleteMemberListener deleteListener) {
        this.context = context;
        this.members = members;
        this.deleteListener = deleteListener;
    }

    public interface OnDeleteMemberListener {
        void onDeleteMember(FamilyMemberModel member);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        Spinner roleSpinner;

        public ViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.memberName);
            roleSpinner = view.findViewById(R.id.memberRole);
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
        holder.nameText.setText(member.getName());

        // Handle long-click for deletion
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
