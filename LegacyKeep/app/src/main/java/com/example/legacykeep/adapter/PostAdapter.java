package com.example.legacykeep.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.legacykeep.R;
import com.example.legacykeep.model.PostModel;

import java.io.IOException;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<PostModel> postList;
    private final Context context;

    public PostAdapter(Context context, List<PostModel> postList) {
        this.context  = context;
        this.postList = postList;
    }

    public void updateList(List<PostModel> newList) {
        postList.clear();
        postList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int pos) {
        PostModel post = postList.get(pos);

        holder.description.setText(post.getDescription());
        holder.location.setText(post.getLocation());

        // Zdjęcie
        if (post.getImageUri() != null) {
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(post.getImageUri())
                    .into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        // Odtwarzanie audio na przycisku commentButton
        if (post.getAudioUri() != null) {
            holder.commentButton.setVisibility(View.VISIBLE);
            holder.commentButton.setOnClickListener(v -> {
                MediaPlayer mp = new MediaPlayer();
                try {
                    mp.setDataSource(context, post.getAudioUri());
                    mp.prepare();
                    mp.start();
                    mp.setOnCompletionListener(MediaPlayer::release);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            holder.commentButton.setVisibility(View.GONE);
        }

        // Możesz ukryć pozostałe przyciski, jeśli nie używasz:
        holder.likeButton.setVisibility(View.GONE);
        holder.shareButton.setVisibility(View.GONE);
    }

    @Override public int getItemCount() { return postList.size(); }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView   imageView;
        TextView    description, location;
        ImageButton commentButton, likeButton, shareButton;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView      = itemView.findViewById(R.id.postImage);
            description    = itemView.findViewById(R.id.postDescription);
            location       = itemView.findViewById(R.id.postLocation);
            commentButton  = itemView.findViewById(R.id.commentButton);
            likeButton     = itemView.findViewById(R.id.likeButton);
            shareButton    = itemView.findViewById(R.id.shareButton);
        }
    }
}
