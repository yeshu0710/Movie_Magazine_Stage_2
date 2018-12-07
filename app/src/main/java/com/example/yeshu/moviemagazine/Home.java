package com.example.yeshu.moviemagazine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity {

    ProgressDialog progressDialog;
    ArrayList<JsonDataClass> jsonDataClassArrayList;
    private static final String API_key=BuildConfig.API_KEY;
    String movieURL="http://api.themoviedb.org/3/movie/popular?api_key="+API_key;
    Cursor cursorData;
    String bundleKey="myBundleKey";
    String bundleValue="myBundleValue";
    int movieID,movieBackdrop,moviePosterpath,movieTitle,movieOverview,movieReleaseDate;
    int scroll=0;
    GridLayoutManager gridLayoutManager;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        gridLayoutManager=new GridLayoutManager(Home.this,2);
        gridLayout=findViewById(R.id.gridLayout);
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoWiFi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfoData=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((networkInfoWiFi!=null & networkInfoData!=null)&&(networkInfoWiFi.isConnected() | networkInfoData.isConnected()))
        {
           if (savedInstanceState != null){
               bundleValue=savedInstanceState.getString(bundleKey);
               movieURL = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_key;
               new MovieDetails().execute();
              if (Objects.equals(savedInstanceState.getString(bundleKey), getString(R.string.popular))){
                  bundleValue = savedInstanceState.getString(bundleKey);
                  movieURL = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_key;
                  new MovieDetails().execute();
              }else if (Objects.equals(savedInstanceState.getString(bundleKey), getString(R.string.top_rated))){
                  bundleValue = savedInstanceState.getString(bundleKey);
                  movieURL = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_key;
                  new MovieDetails().execute();
              }else if (Objects.equals(savedInstanceState.getString(bundleKey), getString(R.string.myFavorites))){
                  bundleValue = savedInstanceState.getString(bundleKey);
                  new FavoritesList().execute();
              }
           } else {
               movieURL = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_key;
               new MovieDetails().execute();
           }
        }else {
            new AlertDialog.Builder(Home.this).setTitle(R.string.app_name).setMessage(R.string.no_internet)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }
        if (savedInstanceState!=null)
            scroll=savedInstanceState.getInt("Scroll");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bundleValue==getResources().getString(R.string.myFavorites))
        {
            new FavoritesList().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class MovieDetails extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            jsonDataClassArrayList =new ArrayList<>();
            URL url=HttpResponse.bulid(movieURL);
            String response=null;
            try {
                response=HttpResponse.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONArray jsonArray=jsonObject.getJSONArray("results");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject movieinfo=jsonArray.getJSONObject(i);
                    JsonDataClass jsonDataClass;
                    int vote_count=movieinfo.getInt("vote_count");
                    String id=movieinfo.getString("id");
                    String title=movieinfo.getString("title");
                    String poster_path=movieinfo.getString("poster_path");
                    String original_language=movieinfo.getString("original_language");
                    String original_title=movieinfo.getString("original_title");
                    String backdrop_path=movieinfo.getString("backdrop_path");
                    String overview=movieinfo.getString("overview");
                    String release_date=movieinfo.getString("release_date");
                    double vote_average=movieinfo.getDouble("vote_average");
                    double popularity=movieinfo.getDouble("popularity");
                    boolean video=movieinfo.getBoolean("video");
                    boolean adult=movieinfo.getBoolean("adult");

                    jsonDataClass=new JsonDataClass(vote_count,id,title,poster_path,original_language,original_title,backdrop_path,overview,release_date,vote_average,popularity,video,adult);
                    jsonDataClassArrayList.add(jsonDataClass);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            gridLayoutManager=new GridLayoutManager(Home.this,2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(new MovieAdapter(Home.this,jsonDataClassArrayList));
            recyclerView.scrollToPosition(scroll);
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(Home.this);
            progressDialog=ProgressDialog.show(Home.this,"Please Wait","Loading...",true);
            progressDialog.setCancelable(false);
            progressDialog.dismiss();
        }
    }

    static class HttpResponse{
        final static String image_url="https://image.tmdb.org/t/p/w300";

        static URL bulidImgUrl(String path){
            String urlImage;
            urlImage = image_url+""+path;
            URL url=null;
            try {
                url=new URL(urlImage);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;
        }

        static URL buildVidoeUrl(String path, String videoId, String purpuse){
            Uri uri=Uri.parse(path+videoId).buildUpon().appendPath(purpuse).appendQueryParameter("api_key",API_key).build();
            URL url=null;
            try {
                url=new URL(uri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return  url;
        }

       static URL bulid(String img_url){
            Uri uri=Uri.parse(img_url);
            URL url=null;
            try {
                url=new URL(uri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;
        }
        static String getResponseFromHttpUrl(URL url) throws IOException {
            String responseHttp;
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream=new BufferedInputStream(httpURLConnection.getInputStream());
            responseHttp=streamConversion(inputStream);
            return responseHttp;
        }
        static String streamConversion(InputStream inputStream){
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder=new StringBuilder();
            String string;
            try {
                while ((string=bufferedReader.readLine())!=null){
                    stringBuilder.append(string).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  stringBuilder.toString();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked=item.getItemId();
        MovieDetails movieDetails = new MovieDetails();
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoWiFi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfoData=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        switch (itemClicked) {
            case R.id.popular:
                bundleValue=getString(R.string.popular);
                if ((networkInfoWiFi!=null & networkInfoData!=null)&&(networkInfoWiFi.isConnected() | networkInfoData.isConnected())) {
                    movieURL = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_key;
                    movieDetails.execute();
                }
                break;
            case R.id.rating:
                bundleValue=getString(R.string.top_rated);
                if ((networkInfoWiFi!=null & networkInfoData!=null)&&(networkInfoWiFi.isConnected() | networkInfoData.isConnected())) {
                    movieURL = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_key;
                    movieDetails.execute();
                }
                break;
            case R.id.favorites:
                bundleValue=getString(R.string.myFavorites);
                new FavoritesList().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    public class FavoritesList extends AsyncTask<Void,Void,Cursor>{

        @Override
        protected Cursor doInBackground(Void... voids) {
            ContentResolver contentResolver=getContentResolver();
            return contentResolver.query(MyFavoriteContract.FavMovieDetails.Content_URI,
                    null,null,null,null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            ArrayList<JsonDataClass> favMovieData=new ArrayList<>();
            super.onPostExecute(cursor);
            cursorData=cursor;
            movieID = cursorData.getColumnIndex(MyFavoriteContract.FavMovieDetails.Column_MovieID);
            movieTitle=cursorData.getColumnIndex(MyFavoriteContract.FavMovieDetails.Column_Original_Title);
            movieBackdrop=cursorData.getColumnIndex(MyFavoriteContract.FavMovieDetails.Column_Backdrop);
            movieOverview=cursorData.getColumnIndex(MyFavoriteContract.FavMovieDetails.Column_OverView);
            moviePosterpath=cursorData.getColumnIndex(MyFavoriteContract.FavMovieDetails.Column_Poster_Path);
            movieReleaseDate=cursorData.getColumnIndex(MyFavoriteContract.FavMovieDetails.Column_Release_Date);
            while (cursor.moveToNext()){
                String mId=cursorData.getString(movieID);
                String mTitle=cursorData.getString(movieTitle);
                String mBackdrop=cursorData.getString(movieBackdrop);
                String mOverview=cursorData.getString(movieOverview);
                String mPosterpath=cursorData.getString(moviePosterpath);
                String mReleaseDate=cursorData.getString(movieReleaseDate);
                favMovieData.add(new JsonDataClass(mId,mTitle,mBackdrop,mOverview,mPosterpath,mReleaseDate));
            }
            Log.i("cursorData", String.valueOf(favMovieData));
            cursorData.close();
            if (favMovieData.isEmpty()){
                new AlertDialog.Builder(Home.this).setTitle(R.string.app_name).setMessage(R.string.no_favorites)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).show();
            }else {
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(new MovieAdapter(Home.this,favMovieData));
                recyclerView.scrollToPosition(scroll);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(bundleKey, bundleValue);
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoWiFi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfoData=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((networkInfoWiFi!=null & networkInfoData!=null)&&(networkInfoWiFi.isConnected() | networkInfoData.isConnected())) {
            if (!progressDialog.isShowing()) {
                scroll = gridLayoutManager.findFirstVisibleItemPosition();
            }
            outState.putInt("Scroll", scroll);
        }
    }
}


