package com.example.toucan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;

public class Music extends AppCompatActivity {
    private LinearLayout showLayout;
    private BottomNavigationView bottomNavigation;
    private TextView trackShow;
    private ImageView playBtn;
    private ImageView nextBtn;
    private ImageView prevBtn;
    private static final int PERMISSION_REQ = 1;
    private ArrayList<String> arrayListTitle;
    private ArrayList<String> arrayListLocation;
    private MediaPlayer mediaPlayer;
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
        showLayout = findViewById(R.id.showLayout);


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
            if(MusicMediaPlayer.currentIndex == 0){
                return;
            }
            MusicMediaPlayer.currentIndex--;
            playMusic(MusicMediaPlayer.currentIndex);
        });
        nextBtn.setOnClickListener(view->{
            if(MusicMediaPlayer.currentIndex == listView.getCount()-1){
                return;
            }
            MusicMediaPlayer.currentIndex++;
            playMusic(MusicMediaPlayer.currentIndex);
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
        }
        if(ContextCompat.checkSelfPermission(Music.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(Music.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(Music.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQ);
            } else {
                ActivityCompat.requestPermissions(Music.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQ);
            }
        }
        else {
            init();
        }
        if(mediaPlayer.isPlaying()){
            trackShow.setText(arrayListTitle.get(MusicMediaPlayer.currentIndex));
            trackShow.setSelected(true);
            playBtn.setImageResource(R.drawable.baseline_pause_24);
        }
    }
    private void init() {
        listView = findViewById(R.id.musicList);
        arrayListTitle = new ArrayList<>();
        arrayListLocation = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListTitle);
        listView.setAdapter(adapter);
        mediaPlayer = MusicMediaPlayer.getMediaPlayer();
        listView.setOnItemClickListener((parent, view, position, id)->{
            MusicMediaPlayer.currentIndex = position;
            playMusic(MusicMediaPlayer.currentIndex);
            showLayout.setVisibility(View.GONE);
            replaceFragment(new PlayMusic(arrayListTitle.get(position),arrayListLocation.get(position),mediaPlayer));
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null).commit();
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
        trackShow.setSelected(true);
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