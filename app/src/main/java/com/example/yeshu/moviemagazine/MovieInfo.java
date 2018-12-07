package com.example.yeshu.moviemagazine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieInfo extends AppCompatActivity {

    String[] myMovieDetails;
    String videoURL="https://api.themoviedb.org/3/movie/";
    String movieId;
    ContentValues contentValues;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.id_toolbar) Toolbar toolbar;
    @BindView(R.id.background_poster) ImageView background_photo;
    @BindView(R.id.poster_of_the_movie) ImageView poster;
    @BindView(R.id.movie_title_tv) TextView title;
    @BindView(R.id.overview_tv) TextView overview;
    @BindView(R.id.rating_tv) TextView rating;
    @BindView(R.id.release_date_tv) TextView release_date;
    @BindView(R.id.is_adult) TextView adult;
    @BindView(R.id.favorite_button) ImageView favorite_button;
    int favdisplay = 0;
    @BindView(R.id.youtube_recycleview) RecyclerView recyclerView;
    @BindView(R.id.review_recycleview) RecyclerView reviewRecycleView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        myMovieDetails=getIntent().getStringArrayExtra("details");
        movieId=myMovieDetails[1];

        Picasso.with(this).load("https://image.tmdb.org/t/p/w500/"+myMovieDetails[6]).into(background_photo);
        Picasso.with(this).load("https://image.tmdb.org/t/p/w500/"+myMovieDetails[3]).into(poster);
        Uri urisel = Uri.parse(MyFavoriteContract.FavMovieDetails.Content_URI + "/*");
        Cursor cursor=getContentResolver().query(urisel,null,movieId,null,null);
        if (cursor.getCount()>0)
        {
            favorite_button.setImageResource(R.mipmap.ic_fav);
            favdisplay=1;
        }
        else
        {
            favdisplay=0;
        }
        title.setText(myMovieDetails[5]);

        rating.setText(myMovieDetails[9]);

        release_date.setText(myMovieDetails[8]);

        overview.setText("\t" + myMovieDetails[7]);

        Boolean s= Boolean.valueOf(myMovieDetails[12]);
        if(s){
            adult.setText(R.string.adult_18);
        }
        else {
            adult.setText(R.string.under_age);
        }

        collapsingToolbarLayout.setTitle(myMovieDetails[2]);

        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favdisplay==1) {
                    getContentResolver().delete(MyFavoriteContract.FavMovieDetails.Content_URI, MyFavoriteContract.FavMovieDetails.Column_MovieID + " =? ", new String[]{movieId});
                    Toast.makeText(MovieInfo.this, R.string.remove_fav,Toast.LENGTH_SHORT).show();
                    favdisplay=0;
                    favorite_button.setImageResource(R.mipmap.ic_not_fav);
                }else {

                    contentValues=new ContentValues();
                    contentValues.put(MyFavoriteContract.FavMovieDetails.Column_MovieID,myMovieDetails[1]);
                    contentValues.put(MyFavoriteContract.FavMovieDetails.Column_Original_Title,myMovieDetails[5]);
                    contentValues.put(MyFavoriteContract.FavMovieDetails.Column_Backdrop,myMovieDetails[6]);
                    contentValues.put(MyFavoriteContract.FavMovieDetails.Column_Poster_Path,myMovieDetails[3]);
                    contentValues.put(MyFavoriteContract.FavMovieDetails.Column_OverView,myMovieDetails[7]);
                    contentValues.put(MyFavoriteContract.FavMovieDetails.Column_Rating,myMovieDetails[9]);
                    contentValues.put(MyFavoriteContract.FavMovieDetails.Column_Release_Date,myMovieDetails[8]);
                    favdisplay=1;
                    Uri uri = getContentResolver().insert(Uri.parse(MyFavoriteContract.FavMovieDetails.Content_URI+ ""),contentValues);
                    if(uri != null){
                        Toast.makeText(MovieInfo.this, uri.toString(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(MovieInfo.this, R.string.add_to_fav,Toast.LENGTH_SHORT).show();
                    DataBaseHelper dataBaseHelper1=new DataBaseHelper(MovieInfo.this);
                    dataBaseHelper1.showFavoriteMovies();
                    favorite_button.setImageResource(R.mipmap.ic_fav);
                }
            }
        });
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoWiFi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfoData=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((networkInfoWiFi!=null & networkInfoData!=null)&&(networkInfoWiFi.isConnected() | networkInfoData.isConnected())){
            new MovieTrailer().execute("videos");
            new MovieReview().execute("reviews");
        }else {
            Toast.makeText(MovieInfo.this,"No Internet Connection to display Trailers and Reviews",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public  class MovieTrailer extends AsyncTask<String,Void,Void>{

        ArrayList<YoutubeJsonData> youtubeJsonData= new ArrayList<>();

        @Override
        protected Void doInBackground(String... strings) {
            URL url= Home.HttpResponse.buildVidoeUrl(videoURL,movieId,strings[0]);
            String response="";
            try {
                response= Home.HttpResponse.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONArray jsonArray=jsonObject.getJSONArray("results");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String id=jsonObject1.getString("id");
                    String lang=jsonObject1.getString("iso_639_1");
                    String country=jsonObject1.getString("iso_3166_1");
                    String key=jsonObject1.getString("key");
                    String name=jsonObject1.getString("name");
                    String size=jsonObject1.getString("size");
                    String site=jsonObject1.getString("site");
                    String video_url="http://www.youtube.com/watch?v="+""+key;
                    String type=jsonObject1.getString("type");
                    YoutubeJsonData youtubeJsonData1=new YoutubeJsonData(id,lang,country,key,name,size,site,video_url,type);
                    youtubeJsonData.add(youtubeJsonData1);
                    }
                } catch (JSONException e) {
                e.printStackTrace();
                }
                return null;
            }
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.setLayoutManager(new LinearLayoutManager(MovieInfo.this));
            recyclerView.setAdapter(new YoutubeAdapter(MovieInfo.this,youtubeJsonData));
            }
       }

        @SuppressLint("StaticFieldLeak")
        public class MovieReview extends AsyncTask<String,Void,Void>{

        ArrayList<ReviewJsonData> reviewJsonData=new ArrayList<>();


        @Override
        protected Void doInBackground(String... strings) {
            URL reviewURL= Home.HttpResponse.buildVidoeUrl(videoURL,movieId,strings[0]);
            String reviewResponse="";
            try {
                reviewResponse=Home.HttpResponse.getResponseFromHttpUrl(reviewURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject=new JSONObject(reviewResponse);
                JSONArray jsonArray=jsonObject.getJSONArray("results");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String author=jsonObject1.getString("author");
                    String content=jsonObject1.getString("content");
                    String id=jsonObject1.getString("id");
                    String url=jsonObject1.getString("url");
                    ReviewJsonData reviewJsonData1=new ReviewJsonData(content,author,id,url);
                    reviewJsonData.add(reviewJsonData1);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            reviewRecycleView.setLayoutManager(new LinearLayoutManager(MovieInfo.this));
            reviewRecycleView.setAdapter(new ReviewAdapter(MovieInfo.this, reviewJsonData));
        }
    }
}
