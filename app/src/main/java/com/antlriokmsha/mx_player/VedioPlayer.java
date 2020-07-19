package com.antlriokmsha.mx_player;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class VedioPlayer extends Activity {

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_player);

        String path = getIntent().getStringExtra("file_path");
        videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(path);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        videoView.setVisibility(View.VISIBLE);
        videoView.setZOrderOnTop(true);
        videoView.start();

        ProgressBar progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

    }
}