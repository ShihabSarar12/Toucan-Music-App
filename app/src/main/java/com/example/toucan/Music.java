package com.example.toucan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;

public class Music extends AppCompatActivity {
    private static int currentPos;
    private BottomNavigationView bottomNavigation;
    private TextView trackShow;
    private ImageView playBtn;
    private ImageView nextBtn;
    private ImageView prevBtn;
    private static final int PERMISSION_REQ = 1;
    private ArrayList<String> arrayListTitle;
    private ArrayList<String> arrayListLocation;
    private ArrayList<String> arrayListMedia;
    public static MediaPlayer mediaPlayer;
    private ListView listView;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        playBtn = findViewById(R.id.playBtn);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        trackShow = findViewById(R.id.trackShow);

        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottomHome){
                startActivity(new Intent(Music.this, Home.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottomSearch) {
                return true;
            } else if (item.getItemId() == R.id.bottomMusic) {
                return true;
            } else if (item.getItemId() == R.id.bottomPerson) {
                return true;
            }
            return false;
        });
        playBtn.setOnClickListener(view->{
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                playBtn.setImageResource(R.drawable.baseline_play_arrow_24);
            }else{
                mediaPlayer.start();
                playBtn.setImageResource(R.drawable.baseline_pause_24);
            }
        });
        prevBtn.setOnClickListener(view->{
            if(currentPos == 0){
                return;
            }
            currentPos--;
            playMusic(currentPos);
        });
        nextBtn.setOnClickListener(view->{
            if(currentPos == listView.getCount()-1){
                return;
            }
            currentPos++;
            playMusic(currentPos);
        });
        if(ContextCompat.checkSelfPermission(Music.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(Music.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(Music.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQ);
            } else {
                ActivityCompat.requestPermissions(Music.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQ);
            }
        } else {
            init();
        }
    }
    private void init() {
        listView = findViewById(R.id.musicList);
        arrayListTitle = new ArrayList<>();
        arrayListLocation = new ArrayList<>();
        arrayListMedia = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListTitle);
        listView.setAdapter(adapter);
        mediaPlayer = new MediaPlayer();
        listView.setOnItemClickListener((parent, view, position, id)->{
            currentPos = position;
            playMusic(currentPos);
        });
    }
    private void playMusic(int position){
        mediaPlayer.reset();
        Uri myUri = Uri.parse(arrayListLocation.get(position));
        try {
            mediaPlayer.setDataSource(Music.this, myUri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        trackShow.setText(arrayListTitle.get(position));
        playBtn.setImageResource(R.drawable.baseline_pause_24);
    }
    private void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null,null,null);
        if(songCursor != null && songCursor.moveToFirst()){
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentLocation = songCursor.getString(songLocation);
                arrayListTitle.add(currentTitle);
                arrayListLocation.add(currentLocation);
            } while(songCursor.moveToNext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQ: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(Music.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        init();
                    }
                } else {
                    Toast.makeText(this, "No Permission Granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }
}