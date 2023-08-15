package com.example.toucan;

import static com.example.toucan.AlbumDetailsAdapter.albumFiles;
import static com.example.toucan.Music.musicFiles;
import static com.example.toucan.Music.repeatBoolean;
import static com.example.toucan.Music.shuffleBoolean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;
public class PlayerActivity extends AppCompatActivity {
    TextView song_name,artist_name,duration_played,duration_total;
    ImageView cover_art,nextBtn,prevBtn,BackBtn,shuffleBtn,repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int position=-1;
    static ArrayList<MusicFiles>listSong=new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler=new Handler();
    private Thread playThread,prevThread,nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        getIntenMethod();
        song_name.setText(listSong.get(position).getTitle());
        artist_name.setText(listSong.get(position).getArtist());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(mediaPlayer !=null && fromUser)
                {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer !=null)
                {
                    int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_played.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffleBoolean)
                {
                    shuffleBoolean=false;
                    shuffleBtn.setImageResource(R.drawable.baseline_shuffle_off_24);
                }
                else{
                    shuffleBoolean=true;
                    shuffleBtn.setImageResource(R.drawable.baseline_shuffle_on__24);
                }

            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatBoolean)
                {
                    repeatBoolean=false;
                    repeatBtn.setImageResource(R.drawable.baseline_repeat_off_24);
                }
                else {

                    repeatBoolean=true;
                    repeatBtn.setImageResource(R.drawable.baseline_repeat_on_24);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {
        prevThread=new Thread(){
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean)
            {
                position=getRandom(listSong.size()-1);
            }
            else if(!shuffleBoolean && !repeatBoolean)
            {

                position=((position -1) < 0 ?(listSong.size()-1): (position-1));
            }

            uri=Uri.parse(listSong.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer !=null)
                    {
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.baseline_pause_24);
            mediaPlayer.start();

        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean)
            {
                position=getRandom(listSong.size()-1);
            }
            else if(!shuffleBoolean && !repeatBoolean)
            {

                position=((position -1) < 0 ?(listSong.size()-1): (position-1));
            }
            uri=Uri.parse(listSong.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer !=null)
                    {
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);

        }

    }

    private void nextThreadBtn() {
        nextThread=new Thread(){
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked() {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean)
            {
                position=getRandom(listSong.size()-1);
            }
            else if(!shuffleBoolean && !repeatBoolean)
            {
                position=((position +1)% listSong.size());

            }

            uri=Uri.parse(listSong.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer !=null)
                    {
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.baseline_pause_24);
            mediaPlayer.start();

        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean)
            {
                position=getRandom(listSong.size()-1);
            }
            else if(!shuffleBoolean && !repeatBoolean)
            {
                position=((position +1)% listSong.size());

            }

            uri=Uri.parse(listSong.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer !=null)
                    {
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);

        }

    }

    private int getRandom(int i) {
        Random random=new Random();
        return  random.nextInt(i+1);
    }

    private void playThreadBtn() {
        playThread=new Thread(){
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if(mediaPlayer.isPlaying())
        {
            playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer !=null)
                    {
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });

        }
        else {
            playPauseBtn.setImageResource(R.drawable.baseline_pause_24);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer !=null)
                    {
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
    }

    private String formattedTime(int mCurrentPosition) {
        String totalout="";
        String totalNew="";
        String seconds=String.valueOf(mCurrentPosition % 60);
        String minutes=String.valueOf(mCurrentPosition / 60);
        totalout=minutes + ":" +seconds;
        totalNew=minutes + ":" + "0" +seconds;
        if(seconds.length()==1) {

            return totalNew;
        }
        else {

            return totalout;
        }
    }

    private void getIntenMethod() {
        position=getIntent().getIntExtra("position",-1);
        String sender=getIntent().getStringExtra("sender");
        if(sender !=null && sender.equals("albumDetails")) {
            listSong = albumFiles;
        }
        else {
            listSong = musicFiles;
        }
        listSong=musicFiles;
        if(listSong!=null) {
            playPauseBtn.setImageResource(R.drawable.baseline_pause_24);
            uri=Uri.parse(listSong.get(position).getPath());
        }
        if(mediaPlayer !=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        metaData(uri);
    }

    private void initViews() {
        song_name=findViewById(R.id.song_name);
        artist_name=findViewById(R.id.song_artist);
        duration_played=findViewById(R.id.durationPlayed);
        duration_total=findViewById(R.id.durationTotal);
        cover_art=findViewById(R.id.cover_art);
        BackBtn=findViewById(R.id.back_btn);
        nextBtn=findViewById(R.id.id_next);
        prevBtn=findViewById(R.id.id_prev);
        playPauseBtn=findViewById(R.id.play_pause);
        seekBar=findViewById(R.id.seekBar);
        shuffleBtn=findViewById(R.id.id_shuffle);
        repeatBtn=findViewById(R.id.id_repeat);
    }
    private void metaData(Uri uri) {
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal=Integer.parseInt(listSong.get(position).getDuration())/1000;
        duration_total.setText(formattedTime(durationTotal));
        byte[] art =retriever.getEmbeddedPicture();
        if(art !=null)
        {
            Glide.with(this).asBitmap().load(art).into(cover_art);
        }
        else {
            Glide.with(this).asBitmap().load(R.drawable.album).into(cover_art);
        }
    }
}