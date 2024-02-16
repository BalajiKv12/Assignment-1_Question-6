package com.example.musicplayer_assignment_q6;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Music_RecyclerAdapter extends RecyclerView.Adapter<Music_RecyclerAdapter.MyViewHolder> {

    Context context;
    //creating arraylist of file objects
    ArrayList<music> song = new ArrayList<>();

    //creating an instance of fileRecyclerInterface
MusicOnclickListener musicOnclickListener;
    //creating constructor for adapter
    public Music_RecyclerAdapter(Context context, ArrayList<music> song,MusicOnclickListener musicOnclickListener) {
        this.context = context;
        this.song = song;
        this.musicOnclickListener=musicOnclickListener;
    }

    @NonNull
    @Override
    //below method used to inflate the view when user scrolls up/down to show the details which couldn't fit the screen

    public Music_RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardlayout,parent,false);
        return new Music_RecyclerAdapter.MyViewHolder(view);
    }

    @Override
    @SuppressLint({"RecyclerView", "SetTextI18n"})
    //onBindViewHolder method for setting details in view for all files one by one by using their position
    public void onBindViewHolder(@NonNull Music_RecyclerAdapter.MyViewHolder holder,  int position) {

        //setting the details of the files to show
        holder.name.setText(song.get(position).getTitle());

        //formatting size in MB
        float sizeinMb = Integer.parseInt(song.get(position).getSize())/1000000f;
        DecimalFormat sizeFormat = new DecimalFormat("#.##");
        //setting the size of the folder
        holder.size.setText(sizeFormat.format(sizeinMb)+" MB");

        holder.image.setImageResource(R.drawable.audio);

        //below method is for setting on click response of the view
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                musicOnclickListener.onClick(song,position);

            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                musicOnclickListener.onLongClick(song.get(position),position);
                return true;
            }
        });
    }


    //getItemcount method return the size of the items to be displayed
    @Override
    public int getItemCount() {
        return song.size();
    }


    //MyViewHolder method for setting the view for recycle view
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name,size;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //assigning the views by finding their respective id
            image = itemView.findViewById(R.id.fileImage);
            name = itemView.findViewById(R.id.fileName);
            size = itemView.findViewById(R.id.fileSize);
            cardView=itemView.findViewById(R.id.Cardlayout);

        }
    }
}
