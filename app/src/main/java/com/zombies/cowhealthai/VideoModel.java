package com.zombies.cowhealthai;

public class VideoModel {
    private String title;
    private String videoId; // Store only the video ID

    public VideoModel(String title, String videoId) {
        this.title = title;
        this.videoId = videoId;
    }

    public String getTitle() { return title; }
    public String getVideoId() { return videoId; }

    // Automatically generate YouTube thumbnail URL
    public String getThumbnailUrl() {
        return "https://img.youtube.com/vi/" + videoId + "/0.jpg";
    }
}
