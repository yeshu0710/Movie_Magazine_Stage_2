package com.example.yeshu.moviemagazine;

/*
 * Created by Yeshu on 29-05-2018.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import static com.example.yeshu.moviemagazine.MyFavoriteContract.FavMovieDetails.Table_Name;

public class DataBaseHelper extends SQLiteOpenHelper{

    private static final int  version=1;
    Context c;

    DataBaseHelper(Context context) {
        super(context, Table_Name, null, version);
        c=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String Movie_Table = "CREATE TABLE " + Table_Name + "("
                + MyFavoriteContract.FavMovieDetails.Column_MovieID + " INTEGER  PRIMARY KEY " + ","
                + MyFavoriteContract.FavMovieDetails.Column_Rating + " TEXT" + ","
                + MyFavoriteContract.FavMovieDetails.Column_Poster_Path + " TEXT" + ","
                + MyFavoriteContract.FavMovieDetails.Column_Original_Title + " TEXT  " + ","
                + MyFavoriteContract.FavMovieDetails.Column_Backdrop + " TEXT" + ","
                + MyFavoriteContract.FavMovieDetails.Column_OverView + " TEXT" + ","
                + MyFavoriteContract.FavMovieDetails.Column_Release_Date + " TEXT" + ")";

        sqLiteDatabase.execSQL(Movie_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Table_Name);
    }

    public Void showFavoriteMovies() {
        ArrayList<JsonDataClass> jsonDataClasses = new ArrayList<>();
        String queryselected = "SELECT * FROM " + MyFavoriteContract.FavMovieDetails.Table_Name;
        SQLiteDatabase database = getReadableDatabase();
        Cursor moviedetails = database.rawQuery(queryselected, null);
        String[] data12 = new String[10];
        String p = "";
        if (moviedetails.moveToFirst()) {
            do {
                data12[0] = String.valueOf(moviedetails.getInt(0));
                data12[1] = moviedetails.getString(1);
                data12[2] = moviedetails.getString(2);
                data12[3] = moviedetails.getString(3);
                data12[4] = moviedetails.getString(4);
                data12[5] = moviedetails.getString(5);
                data12[6] = moviedetails.getString(6);
                p = p + data12[1] + "\n" + data12[2] + "\n" + data12[3] + "\n" + data12[4] + "\n" + data12[5] + "\n" + data12[6] + "\n";
            } while (moviedetails.moveToNext());
        }
        database.close();
        return null;
    }
}
