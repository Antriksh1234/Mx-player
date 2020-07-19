package com.antlriokmsha.mx_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> filePath;
    ArrayList<String> video_names;
    class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder>{

        private ArrayList<String> title_list;
        private ArrayList<String> path_list;
        VideoListAdapter(ArrayList<String> title_list, ArrayList<String> path_list){
            this.title_list = title_list;
            this.path_list = path_list;
        }

        @NonNull
        @Override
        public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.row_vedios,parent,false);

            return new VideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VideoViewHolder holder, final int position) {
            String title = title_list.get(position);
            holder.title_of_video.setText(title);
            Glide
                    .with(MainActivity.this)
                    .asBitmap()
                    .load(Uri.fromFile(new File(path_list.get(position))))
                    .into(holder.thumbnail);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,VedioPlayer.class);
                    intent.putExtra("file_path",path_list.get(position));
                    intent.putExtra("file_name",title_list.get(position));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return title_list.size();
        }

        class VideoViewHolder extends RecyclerView.ViewHolder{
            ImageView thumbnail;
            ImageView menu;
            TextView title_of_video;
            public VideoViewHolder(@NonNull View itemView) {
                super(itemView);

                thumbnail = itemView.findViewById(R.id.thumbnail);
                menu = itemView.findViewById(R.id.menu);
                title_of_video = itemView.findViewById(R.id.title_of_video);
            }
        }
    }

    //Gets all videos from device and populate their name in the arrayList
    private void getAllVideosFromFolder(File dir){
        for(File f : dir.listFiles()){
            if(f.isDirectory()){
                getAllVideosFromFolder(f);
            } else {
                if(f.getAbsolutePath().contains(".mp4")){
                    video_names.add(f.getName());
                    filePath.add(f.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("Why we need this?")
                    .setMessage("We need to access your video files to play them here. Please grant the permission to read files")
                    .setPositiveButton("Ok",null);

            dialog.show();
        } else {
            if(requestCode == 0 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getAllVideosFromFolder(Environment.getExternalStorageDirectory());
                VideoListAdapter adapter = new VideoListAdapter(video_names, filePath);
                recyclerView.setAdapter(adapter);
            } else {
                //Permission denied forever ask for enabling in settings
                Toast.makeText(this, "You denied permission forever, enable it from settings", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.video_recyclerview);

        video_names = new ArrayList<>();

        filePath = new ArrayList<>();

        String folder_path = getIntent().getStringExtra("folder_path");
        File folder_file = new File(folder_path);
        getAllVideosFromFolder(folder_file);

        VideoListAdapter adapter = new VideoListAdapter(video_names, filePath);
        recyclerView.setAdapter(adapter);

    }
}