package com.example.legacykeep.adapter;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.legacykeep.API.ApiClient;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legacykeep.R;
import com.example.legacykeep.model.PostModel;

import java.io.IOException;
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
        holder.createdAt.setText(post.getCreatedAt());

        // Load image
        String imageUrl = preprocessUrl(post.getImagePath());
        if (imageUrl != null) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imageView);
        }

        // Handle audio playback
        String audioUrl = preprocessUrl(post.getAudioPath());
        if (audioUrl != null) {
            holder.audioButton.setVisibility(View.VISIBLE);
            holder.audioButton.setOnClickListener(v -> {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(audioUrl);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(holder.itemView.getContext(), "Playing audio", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(holder.itemView.getContext(), "Failed to play audio", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.audioButton.setVisibility(View.GONE);
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

    private String preprocessUrl(String url) {
        if (url != null && !url.startsWith("http")) {
            return ApiClient.getBaseUrl() + (url.startsWith("/") ? url : "/" + url).replace("\\", "/");
        }
        return url;
    }

}