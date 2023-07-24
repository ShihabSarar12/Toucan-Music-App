package com.example.toucan;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.IOException;

public class PlayMusic extends Fragment {
    private String songTitle;
    private String songLocation;
    private MediaPlayer mediaPlayer;
    private Button backBtn;
    private TextView title;
    private FrameLayout showFrameLayout;
    private View view;

    public PlayMusic() {
    }
    public PlayMusic(String songTitle, String songLocation,MediaPlayer mediaPlayer) {
        this.songTitle = songTitle;
        this.songLocation = songLocation;
        this.mediaPlayer = mediaPlayer;
    }

    public PlayMusic(int contentLayoutId, String songTitle, String songLocation) {
        super(contentLayoutId);
        this.songTitle = songTitle;
        this.songLocation = songLocation;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongLocation() {
        return songLocation;
    }

    public void setSongLocation(String songLocation) {
        this.songLocation = songLocation;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backBtn = view.findViewById(R.id.backBtn);
        title = view.findViewById(R.id.title);
        showFrameLayout = view.findViewById(R.id.showFrameLayout);

        title.setText(songTitle);
        backBtn.setOnClickListener(btnView->{
            showFrameLayout.setVisibility(View.GONE);
            requireActivity().onBackPressed();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_play_music, container, false);
        return view;
    }
}