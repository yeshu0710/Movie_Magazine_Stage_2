package com.example.yeshu.moviemagazine;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    DataBaseHelper dataBaseHelper;
    public static final int FAV_MOVIE_DB =70,
            FAV_MOVIE_DB_ID=120;

    private static final UriMatcher uriMatcher=buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MyFavoriteContract.Authority,MyFavoriteContract.Path_table,FAV_MOVIE_DB);
        uriMatcher.addURI(MyFavoriteContract.Authority, MyFavoriteContract.Path_table + "/*", FAV_MOVIE_DB_ID);
        return uriMatcher;
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase sqDatabase = dataBaseHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int favMoviedeleted = 0;
        if (selection == null) {
            selection = "1";
        }
        switch (match) {
            case FAV_MOVIE_DB:
                favMoviedeleted = sqDatabase.delete(MyFavoriteContract.FavMovieDetails.Table_Name, selection, selectionArgs);
                break;
           default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        if (favMoviedeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return favMoviedeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        long id;
        int match = uriMatcher.match(uri);
        Uri uriMatched = null;
        switch (match) {
            case FAV_MOVIE_DB:
                id = db.insert(MyFavoriteContract.FavMovieDetails.Table_Name, null, values);
                if (id > 0) {
                    uriMatched = ContentUris.withAppendedId(MyFavoriteContract.FavMovieDetails.Content_URI, id);
                    Log.i("urimatch",MyFavoriteContract.FavMovieDetails.Content_URI.toString());
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uriMatched;
    }

    @Override
    public boolean onCreate() {
        Context context=getContext();
        dataBaseHelper=new DataBaseHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        final SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case FAV_MOVIE_DB:
                cursor = db.query(MyFavoriteContract.FavMovieDetails.Table_Name, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FAV_MOVIE_DB_ID:
                cursor = db.query(MyFavoriteContract.FavMovieDetails.Table_Name, projection, MyFavoriteContract.FavMovieDetails.Column_MovieID + "=" + selection, null, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
