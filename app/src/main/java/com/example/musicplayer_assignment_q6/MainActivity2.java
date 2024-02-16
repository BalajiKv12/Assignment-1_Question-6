package com.example.musicplayer_assignment_q6;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity2 extends AppCompatActivity {

    TextView songTitle, songStart, songEnd,name,size,composer,artist,path,album;
    ImageView pausePlay, skipPrevious, skipForward,music;
    SeekBar playbar,volumeBar;
    AudioManager volumeManager;
    MediaPlayer mediaPlayer = new MediaPlayer();
    ArrayList<music> song = new ArrayList<>();
    music currentSong;

    int i=0;
    int position;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //getting song from mainactivity
        position = Integer.parseInt(getIntent().getStringExtra("position"));
        song = (ArrayList<music>) getIntent().getSerializableExtra("song");

        //mapping variables withs ID's
        songTitle = findViewById(R.id.songTitle);
        songStart = findViewById(R.id.startTime);
        songEnd = findViewById(R.id.stopTime);
        pausePlay = findViewById(R.id.pause_play);
        skipForward = findViewById(R.id.next);
        skipPrevious = findViewById(R.id.previous);
        music = findViewById(R.id.musicImage);
        playbar = findViewById(R.id.playBar);
        volumeBar = findViewById(R.id.volumeBar);
        composer = findViewById(R.id.songAuthor);
        name = findViewById(R.id.songName);
        size = findViewById(R.id.song_size);
        artist = findViewById(R.id.song_artist);
        path = findViewById(R.id.song_path);
        album=findViewById(R.id.song_album);

        //setting the song and playing it
        setSong();


        //runOnUiThread updates the Ui for every 100 milli seconds
        MainActivity2.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    playbar.setProgress(mediaPlayer.getCurrentPosition());
                    songStart.setText(convertToMinutes(mediaPlayer.getCurrentPosition()+""));

                    if(mediaPlayer.isPlaying()){
                        //setting the play_pause image to pause
                        pausePlay.setImageResource(R.drawable.pause);
                        //rotating the music image
                        music.setRotation(i++);
                    }else{
                        //setting the play_pause image to play
                        pausePlay.setImageResource(R.drawable.play);
                        //stopping the rotation
                        music.setRotation(i);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });

    try{
        //setting onClick response for playBar to fast forward or listen a separate portion of hte song
        playbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

            //getting the audio services to manage the volume of the device using a seekBar
            volumeManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeBar.setMax(volumeManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeBar.setProgress(volumeManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));
            volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {}
                @Override
                public void onStartTrackingTouch(SeekBar arg0) {}
                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    volumeManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @SuppressLint("SetTextI18n")
    void setSong() {
        //getting the current song
        currentSong = song.get(position);
        //setting the song name of the song
        songTitle.setText(currentSong.getTitle());
        //setting the details of the music to show
        name.setText(currentSong.getTitle());
        //formatting size in MB
        float sizeinMb = Integer.parseInt(currentSong.getSize())/1000000f;
        DecimalFormat sizeFormat = new DecimalFormat("#.##");
        //setting the size of the folder
        size.setText(sizeFormat.format(sizeinMb)+" MB");
        artist.setText(currentSong.getArtist());
        composer.setText(currentSong.getComposer());
        path.setText(currentSong.getPath());
        album.setText(currentSong.getAlbum());
        //setting the duration of the song
        songEnd.setText(convertToMinutes(currentSong.getDuration()));

        //onclick response for pausing and playing the song
        pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                else
                    mediaPlayer.start();
            }
        });

        //onclick response for skipping to next song
        skipForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position== song.size()-1)
                    return;
                position +=1;
                mediaPlayer.reset();
                //setting new song
                setSong();
            }
        });

        //onclick response for going to previous song
        skipPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position== 0)
                    return;
                position -=1;
                mediaPlayer.reset();
                //setting new song
                setSong();
            }
        });
        //method to play the song
        playSong();
    }

    private void playSong(){
        //resetting the player if any song played before
        mediaPlayer.reset();
        try {
            //getting song location to play
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            //setting playbar to zero
            playbar.setProgress(0);
            playbar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //converting milli seconds to minutes
    @SuppressLint("DefaultLocale")
    public static String convertToMinutes(String duration) {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}