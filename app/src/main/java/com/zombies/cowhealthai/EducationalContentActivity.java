package com.zombies.cowhealthai;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EducationalContentActivity extends AppCompatActivity {
    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    private List<VideoModel> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educational);

        videoRecyclerView = findViewById(R.id.videoRecyclerView);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // 1 column

        videoList = new ArrayList<>();
        loadVideos();
        videoAdapter = new VideoAdapter(this, videoList);
        videoRecyclerView.setAdapter(videoAdapter);
    }

    private void loadVideos() {
        videoList.add(new VideoModel("All About Farms", "KJzmM9SL0mA"));
        videoList.add(new VideoModel("Test Video 2", "3JZ_D3ELwOQ")); // SpaceX Falcon Heavy Test Flight
        videoList.add(new VideoModel("Test Video 3", "L3pk_TBkihU")); // National Geographic - Lions Documentary
        videoList.add(new VideoModel("Test Video 4", "M7lc1UVf-VE")); // YouTube API Test Video
        videoList.add(new VideoModel("Test Video 5", "e-ORhEE9VVg")); // Taylor Swift - Blank Space
    }
}