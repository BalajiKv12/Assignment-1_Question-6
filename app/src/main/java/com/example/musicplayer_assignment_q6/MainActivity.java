package com.example.musicplayer_assignment_q6;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements MusicOnclickListener{

    //creating an instance of room database
    MusicRoomDB musicRoomDB;
    RecyclerView recyclerView;

    //creating arrayList of music
    private ArrayList<music> song = new ArrayList<>();
    //creating instance of a Music_RecyclerAdapter
    private Music_RecyclerAdapter musicAdapter ;
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checking storage perimmison
        boolean permission = checkStoragePermissions();
        if(!permission)
        {
            requestForStoragePermissions();
        }

        //getting audio files and its details from the storage
        String projection []= {MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DURATION,
                                MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.DATE_ADDED,MediaStore.Audio.Media.ARTIST,
                                MediaStore.Audio.Media.COMPOSER,MediaStore.Audio.Media.SIZE};

        //selecting the media file shoud be a audio file
        String Select = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        //storing the audio files in cursor
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,Select,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String title, path, duration,album,dateAdded,artist,composer,size;
                while (!cursor.isAfterLast()) {
                    //getting the song details from the cursor and saving it the arrayList of songs
                    title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                    artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    composer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER));
                    size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                    song.add(new music(title,path,duration,album,dateAdded,artist,composer,size));
                    cursor.moveToNext();
                }
                //setting adapter with recyclerView
                recyclerView = findViewById(R.id.recycle_Music);
                musicAdapter = new Music_RecyclerAdapter(getApplicationContext(),song,this);
                recyclerView.setAdapter(musicAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        }

        //callback for room database
        RoomDatabase.Callback myCallBack = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };

        //building room database
        musicRoomDB = Room.databaseBuilder(getApplicationContext(),MusicRoomDB.class,"musicDB").addCallback(myCallBack).build();

    }

    //onClickListener for songs
    @Override
    public void onClick(ArrayList<music> songs,int position) {
        //starting new activity
        Intent player = new Intent(MainActivity.this, MainActivity2.class);
        //sending song and its adapter position to play the clicked song
        player.putExtra("song",songs);
        player.putExtra("position",Integer.toString(position));
        startActivity(player);
    }

    @Override
    public void onLongClick(music song, int position) {
        //saving the song in room DB
        addSongInBackgroung(song);
    }

    //cheacking storage permission
    public boolean checkStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11 (R) or above
            return Environment.isExternalStorageManager();
        }
        return false;
    }

    //requesting user to give storage permission
    private void requestForStoragePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);
            }
        }
    }

    //getting the user response of the storage permission
    ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

        @Override
        public void onActivityResult(ActivityResult result) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                //user accepts
                if(Environment.isExternalStorageManager()){
                    Log.d("TAG", "onActivityResult: Manage External Storage Permissions Granted");

                }
                //user rejects
                else{
                    Toast.makeText(MainActivity.this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                    requestForStoragePermissions();
                }
            }
        }
    });

    //adding the song in room database in background
    public void addSongInBackgroung(music song){
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                musicRoomDB.getPersonDAO().addSong(song);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Song saved to room DB",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}