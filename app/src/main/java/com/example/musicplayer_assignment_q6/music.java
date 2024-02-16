package com.example.musicplayer_assignment_q6;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//model class for storing all song details
@Entity(tableName = "music")
public class music implements Serializable {

    @ColumnInfo(name = "title")
    @PrimaryKey
    @NonNull
    private String title;
    @ColumnInfo(name = "path")
    String path;
    @ColumnInfo(name = "duration")
    String duration;
    @ColumnInfo(name = "album")
    String album;
    @ColumnInfo(name = "dateAdded")
    String dateAdded;
    @ColumnInfo(name = "artist")
    String artist;
    @ColumnInfo(name = "composer")
    String composer;
    @ColumnInfo(name = "size")
    String size;


    public music(String title, String path, String duration, String album, String dateAdded, String artist, String composer, String size) {
        this.title = title;
        this.path = path;
        this.duration = duration;
        this.album = album;
        this.dateAdded = dateAdded;
        this.artist = artist;
        this.composer = composer;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}