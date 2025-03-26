package com.zombies.cowhealthai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private Context context;
    private List<VideoModel> videoList;

    public VideoAdapter(Context context, List<VideoModel> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoModel video = videoList.get(position);
        holder.titleTextView.setText(video.getTitle());

        // Load YouTube thumbnail using Picasso
        String thumbnailUrl = "https://img.youtube.com/vi/" + video.getVideoId() + "/0.jpg";
        Picasso.get()
                .load(thumbnailUrl)
                .placeholder(R.drawable.placeholder_image)  // Show while loading
                .error(R.drawable.error_image)  // Show on error
                .into(holder.thumbnailImageView);

        // Open YouTube Player on click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("videoId", video.getVideoId());  // Pass video ID
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView thumbnailImageView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.videoTitle);
            thumbnailImageView = itemView.findViewById(R.id.videoThumbnail);
        }
    }
}
