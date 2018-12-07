package com.example.yeshu.moviemagazine;

/*
 * Created by Yeshu on 30-05-2018.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyReviewViewHolder> {

    Context c;
    ArrayList<ReviewJsonData> reviewJsonData1;

    ReviewAdapter(MovieInfo movieInfo, ArrayList<ReviewJsonData> reviewJsonData) {
        this.c=movieInfo;
        this.reviewJsonData1=reviewJsonData;
    }


    @NonNull
    @Override
    public ReviewAdapter.MyReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(c).inflate(R.layout.review_view,parent,false);
        return new MyReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyReviewViewHolder holder, int position) {
        ReviewJsonData reviewBind=reviewJsonData1.get(position);
        holder.author_name.setText(reviewBind.getAuthor());
        holder.content.setText("\t\t\t\t"+reviewBind.getMovie_content());
    }

    @Override
    public int getItemCount() {
        return reviewJsonData1.size();
    }

    class MyReviewViewHolder extends RecyclerView.ViewHolder {
        TextView author_name,content;
        MyReviewViewHolder(View itemView) {
            super(itemView);

            author_name=itemView.findViewById(R.id.author_name);
            content=itemView.findViewById(R.id.review_content);

        }
    }
}
