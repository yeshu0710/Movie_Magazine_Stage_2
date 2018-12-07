package com.example.yeshu.moviemagazine;

/*
 * Created by Yeshu on 25-05-2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class YoutubeJsonData implements Parcelable {
    private String videoID;
    private String videoLang;
    private String country;
    private String videoKey;
    private String videoName;
    private String videoSize;
    private String videoSite;
    private String videoURL;
    private String videoType;


    public YoutubeJsonData(String videoID, String videoLang, String country, String videoKey, String videoName, String videoSize, String videoSite, String videoURL, String videoType) {
        this.videoID = videoID;
        this.videoLang = videoLang;
        this.country = country;
        this.videoKey = videoKey;
        this.videoName = videoName;
        this.videoSize = videoSize;
        this.videoSite = videoSite;
        this.videoURL = videoURL;
        this.videoType = videoType;
    }


    private YoutubeJsonData(Parcel in) {
        videoID = in.readString();
        videoLang = in.readString();
        country = in.readString();
        videoKey = in.readString();
        videoName = in.readString();
        videoSize = in.readString();
        videoSite = in.readString();
        videoURL = in.readString();
        videoType = in.readString();
    }

    public static final Creator<YoutubeJsonData> CREATOR = new Creator<YoutubeJsonData>() {
        @Override
        public YoutubeJsonData createFromParcel(Parcel in) {
            return new YoutubeJsonData(in);
        }

        @Override
        public YoutubeJsonData[] newArray(int size) {
            return new YoutubeJsonData[size];
        }
    };

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getVideoLang() {
        return videoLang;
    }

    public void setVideoLang(String videoLang) {
        this.videoLang = videoLang;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(String videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoSite() {
        return videoSite;
    }

    public void setVideoSite(String videoSite) {
        this.videoSite = videoSite;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(videoID);
        parcel.writeString(videoLang);
        parcel.writeString(country);
        parcel.writeString(videoKey);
        parcel.writeString(videoName);
        parcel.writeString(videoSize);
        parcel.writeString(videoSite);
        parcel.writeString(videoURL);
        parcel.writeString(videoType);
    }
}
