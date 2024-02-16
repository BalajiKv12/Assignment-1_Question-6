package com.example.musicplayer_assignment_q6;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {music.class},version=1)
public abstract class MusicRoomDB extends RoomDatabase {

    public abstract musicDAO getPersonDAO();
}
