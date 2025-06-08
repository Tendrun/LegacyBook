package com.example.legacykeep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

    /** Odświeża całą listę i powiadamia adapter */
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
        holder.imageView.setImageResource(post.getImageResId());
        holder.description.setText(post.getDescription());
        holder.location.setText(post.getLocation());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView description, location;
        ImageButton likeButton, commentButton, shareButton;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView      = itemView.findViewById(R.id.postImage);
            description    = itemView.findViewById(R.id.postDescription);
            location       = itemView.findViewById(R.id.postLocation);
            likeButton     = itemView.findViewById(R.id.likeButton);
            commentButton  = itemView.findViewById(R.id.commentButton);
            shareButton    = itemView.findViewById(R.id.shareButton);
        }
    }
}
