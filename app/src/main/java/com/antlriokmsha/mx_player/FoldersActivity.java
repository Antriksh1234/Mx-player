package com.antlriokmsha.mx_player;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class FoldersActivity extends AppCompatActivity {

    ListView folder_listView;
    ArrayList<String> video_folders;
    ArrayList<String> video_folders_path;
    ArrayList<String> folders_absolute_path;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("Why we need this?")
                    .setMessage("We need to access your video files to play them here. Please grant the permission to read files")
                    .setPositiveButton("Ok",null);

            dialog.show();
        } else {
            if(requestCode == 0 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getAllFoldersPathAndName(Environment.getExternalStorageDirectory());
            } else {
                //Permission denied forever ask for enabling in settings
                Toast.makeText(this, "You denied permission forever, enable it from settings", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void getAllFoldersPathAndName(File dir){
        for(File f : dir.listFiles()){
            if(f.isDirectory()){
                getAllFoldersPathAndName(f);
            } else {
                if(f.getAbsolutePath().contains(".mp4")){
                    if(!video_folders_path.contains(f.getParent())){
                        video_folders_path.add(f.getParent());
                        video_folders.add(f.getParent().substring(f.getParent().lastIndexOf('/')+1));
                        folders_absolute_path.add(f.getParentFile().getAbsolutePath());
                    }
                }
            }
        }
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> folders;
        MyAdapter(Context context, int listView_layout, ArrayList<String> folders){
            super(context,listView_layout,video_folders);
            this.context = context;
            this.folders = folders;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view  = getLayoutInflater().inflate(R.layout.listview_layout,null);
            TextView folder_name = view.findViewById(R.id.folder_name);
            folder_name.setText(video_folders.get(position));
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

        folder_listView = findViewById(R.id.folder_listView);

        video_folders = new ArrayList<>();
        video_folders_path = new ArrayList<>();
        folders_absolute_path = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            getAllFoldersPathAndName(Environment.getExternalStorageDirectory());

            MyAdapter adapter = new MyAdapter(this,R.layout.listview_layout,video_folders);

            folder_listView.setAdapter(adapter);

            folder_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent= new Intent(FoldersActivity.this,MainActivity.class);
                    intent.putExtra("folder_path",folders_absolute_path.get(position));
                    startActivity(intent);
                }
            });
        } else {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }

    }
}