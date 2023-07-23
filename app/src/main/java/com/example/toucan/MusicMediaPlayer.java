package com.example.toucan;

import android.media.MediaPlayer;

public class MusicMediaPlayer {
    public static MediaPlayer mediaPlayer;
    public static int currentIndex = -1;
    public static MediaPlayer getMediaPlayer(){
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        return mediaPlayer;
    }
}
