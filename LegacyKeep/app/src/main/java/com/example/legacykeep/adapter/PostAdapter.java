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

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.content.pm.PackageManager;
import android.Manifest;
import android.view.View;
import android.widget.Toast;
import androidx.core.content.FileProvider;
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
        // 1. Pobierz post
        PostModel post = postList.get(position);

        // 2. Ustaw teksty
        holder.description.setText(post.getContent());
        holder.location.setText(post.getAuthorName());
        holder.createdAt.setText(post.getCreatedAt());

        // 3. Wczytaj obrazek przez Glide
        String imageUrl = preprocessUrl(post.getImagePath());
        if (imageUrl != null) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imageView);
        }

        // 4. Obsługa przycisku Bluetooth
        holder.bluetoothButton.setVisibility(View.VISIBLE);
        holder.bluetoothButton.setOnClickListener(v -> {
            Context ctx = holder.itemView.getContext();

            // 4a) Sprawdź uprawnienie BLUETOOTH_CONNECT na Android 12+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ctx.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT)
                            != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ctx, "Brak uprawnień Bluetooth", Toast.LENGTH_SHORT).show();
                return;
            }

            // 4b) Pobierz bitmapę z ImageView
            BitmapDrawable drawable = (BitmapDrawable) holder.imageView.getDrawable();
            if (drawable == null) {
                Toast.makeText(ctx, "Brak obrazu do wysłania", Toast.LENGTH_SHORT).show();
                return;
            }
            Bitmap bitmap = drawable.getBitmap();

            // 4c) Zapisz bitmapę do pliku w cache/images
            File imagesDir = new File(ctx.getCacheDir(), "images");
            if (!imagesDir.exists()) imagesDir.mkdirs();
            File imageFile = new File(imagesDir, "post_" + position + ".jpg");
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ctx, "Błąd zapisu pliku", Toast.LENGTH_SHORT).show();
                return;
            }

            // 4d) Utwórz bezpieczny Uri przez FileProvider
            Uri contentUri = FileProvider.getUriForFile(
                    ctx,
                    ctx.getPackageName() + ".fileprovider",
                    imageFile
            );

            // 4e) Przygotuj Intent do wysyłki
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("image/jpeg");
            sendIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // 4f) Wyświetl chooser (m.in. Bluetooth)
            Intent chooser = Intent.createChooser(sendIntent, "Wyślij obraz przez:");
            ctx.startActivity(chooser);
        });

        // 5. Obsługa przycisku audio
        String audioUrl = preprocessUrl(post.getAudioPath());
        if (audioUrl != null) {
            holder.audioButton.setVisibility(View.VISIBLE);
            holder.audioButton.setOnClickListener(v -> {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(audioUrl);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(holder.itemView.getContext(), "Odtwarzanie audio", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(holder.itemView.getContext(), "Błąd odtwarzania audio", Toast.LENGTH_SHORT).show();
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
        public ImageButton bluetoothButton;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.postDescription);
            location = itemView.findViewById(R.id.postLocation);
            createdAt = itemView.findViewById(R.id.postCreatedAt);
            audioButton = itemView.findViewById(R.id.audioButton);
            imageView = itemView.findViewById(R.id.postImageView); // Ensure this matches the ID in `item_post.xml`
            bluetoothButton= itemView.findViewById(R.id.bluetoothButton);
        }
    }

    private String preprocessUrl(String url) {
        if (url != null && !url.startsWith("http")) {
            return ApiClient.getBaseUrl() + (url.startsWith("/") ? url : "/" + url).replace("\\", "/");
        }
        return url;
    }

}