package com.example.legacykeep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legacykeep.R;
import com.example.legacykeep.model.PostModel;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<PostModel> postList;

    public PostAdapter(List<PostModel> postList) {
        this.postList = postList;
    }

    public void updateList(List<PostModel> newList) {
        this.postList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel post = postList.get(position);
        holder.description.setText(post.getContent());
        holder.location.setText(post.getAuthorName());

        if (post.getImagePath() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(post.getImagePath())
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView description, location, createdAt;
        public ImageButton audioButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.postDescription);
            location = itemView.findViewById(R.id.postLocation);
            createdAt = itemView.findViewById(R.id.postCreatedAt);
            audioButton = itemView.findViewById(R.id.audioButton);
            imageView = itemView.findViewById(R.id.postImageView); // Ensure this matches the ID in `item_post.xml`
        }
    }
}