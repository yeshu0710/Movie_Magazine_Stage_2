package com.example.yeshu.moviemagazine;

/*
 * Created by Yeshu on 30-05-2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

class ReviewJsonData implements Parcelable {
    private String movie_content;
    private String author;
    private String movie_id;
    private String url;

    ReviewJsonData(String movie_content, String author, String movie_id, String url) {
        this.movie_content = movie_content;
        this.author = author;
        this.movie_id = movie_id;
        this.url = url;
    }

    protected ReviewJsonData(Parcel in) {
        movie_content = in.readString();
        author = in.readString();
        movie_id = in.readString();
        url = in.readString();
    }

    public static final Creator<ReviewJsonData> CREATOR = new Creator<ReviewJsonData>() {
        @Override
        public ReviewJsonData createFromParcel(Parcel in) {
            return new ReviewJsonData(in);
        }

        @Override
        public ReviewJsonData[] newArray(int size) {
            return new ReviewJsonData[size];
        }
    };

    public String getMovie_content() {
        return movie_content;
    }

    public String getAuthor() {
        return author;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movie_content);
        parcel.writeString(author);
        parcel.writeString(movie_id);
        parcel.writeString(url);
    }
}
