package com.example.yeshu.moviemagazine;

/*
 * Created by Yeshu on 29-05-2018.
 */

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

class MyFavoriteContract {

    public final static String Authority="com.example.yeshu.moviemagazine";
    public final static Uri Base_URI= Uri.parse("content://"+Authority);
    public final static String Path_table="favoritesList";

    public static final class FavMovieDetails implements BaseColumns{
        public static final Uri Content_URI=Base_URI.buildUpon().appendPath(Path_table).build();

        public static final String Table_Name="favoritesList";
        public static final String Column_MovieID="MovieID";
        public static final String Column_Poster_Path="PosterPath";
        public static final String Column_Backdrop="Backdrop";
        public static final String Column_Rating="Rating";
        public static final String Column_OverView="Overview";
        public static final String Column_Original_Title="OriginalTitle";
        public static final String Column_Release_Date="ReleaseDate";
    }
}
