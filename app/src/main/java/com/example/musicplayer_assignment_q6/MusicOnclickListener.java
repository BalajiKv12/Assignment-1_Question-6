package com.example.musicplayer_assignment_q6;

import java.util.ArrayList;

public interface MusicOnclickListener {

    void onClick(ArrayList<music>music,int position);

    void onLongClick(music music,int position);
}
