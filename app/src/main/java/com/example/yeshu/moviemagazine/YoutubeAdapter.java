package com.example.yeshu.moviemagazine;

/*
 * Created by Yeshu on 30-05-2018.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.MyYoutubeView> {

    private Context c;
    private ArrayList<YoutubeJsonData> videoData;
    private String[] videoDetails=new String[50];

    YoutubeAdapter(Context c, ArrayList<YoutubeJsonData> videoData) {
        this.c = c;
        this.videoData =videoData;
    }

    @NonNull
    @Override
    public YoutubeAdapter.MyYoutubeView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(c).inflate(R.layout.youtube_view,parent,false);
        return new MyYoutubeView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeAdapter.MyYoutubeView holder, int position) {
        final YoutubeJsonData youtubeJsonData=videoData.get(position);
        holder.trailer_name.setText(youtubeJsonData.getVideoName());
        holder.trailer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeJsonData.getVideoURL()));
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoData.size();
    }

    class MyYoutubeView extends RecyclerView.ViewHolder {

        TextView trailer_name;

        MyYoutubeView(View itemView) {
            super(itemView);
            trailer_name=itemView.findViewById(R.id.trailer_name);
            final int position=getLayoutPosition();

            trailer_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoDetails[0]=videoData.get(position).getVideoID();
                    videoDetails[1]=videoData.get(position).getVideoLang();
                    videoDetails[2]=videoData.get(position).getCountry();
                    videoDetails[3]=videoData.get(position).getVideoKey();
                    videoDetails[4]=videoData.get(position).getVideoName();
                    videoDetails[5]=videoData.get(position).getVideoSize();
                    videoDetails[6]=videoData.get(position).getVideoSite();
                    videoDetails[7]=videoData.get(position).getVideoURL();
                    videoDetails[8]=videoData.get(position).getVideoType();
                }
            });
        }
    }
}