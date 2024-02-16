package com.example.musicplayer_assignment_q6;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity_Start extends AppCompatActivity {

    Button internal,roomDB;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);
        internal = findViewById(R.id.internalStorage);
        roomDB = findViewById(R.id.roomDB);

        internal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent internal = new Intent(MainActivity_Start.this, MainActivity.class);
                startActivity(internal);
            }
        });

        roomDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent room = new Intent();
                startActivity(room);
            }
        });
    }
}