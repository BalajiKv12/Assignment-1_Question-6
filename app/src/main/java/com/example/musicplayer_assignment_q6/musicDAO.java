package com.example.musicplayer_assignment_q6;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.ArrayList;

@Dao
public interface musicDAO {

    @Insert
    public void addSong(music music);

    @Query("select * from music")
    public Cursor getAllSongs();

}
