package com.soboleva.vkmusicapp.api.vk.models;

import com.google.gson.annotations.SerializedName;

public class Audio {

    @SerializedName("id")
    private String mID;

    @SerializedName("duration")
    private String mDuration;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("owner_id")
    private String mOwnerID;

    @SerializedName("artist")
    private String mArtist;

    @SerializedName("url")
    private String mURL;

    @SerializedName("lyrics_id")
    private String mLyricsID;

//    идентификатор жанра
//    @SerializedName("genre_id")
//    private String mGenreID;

//    @SerializedName("album_id")
//    private String mAlbumID;


    public String getID() {
        return mID;
    }

    public String getDuration() {
        return mDuration;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOwnerID() {
        return mOwnerID;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getLyricsID() {
        return mLyricsID;
    }

    public String getURL() {
        return mURL;
    }

    @Override
    public String toString() {
        return "Audio{" +
                "mID='" + mID + '\'' +
                ", mDuration='" + mDuration + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mOwnerID='" + mOwnerID + '\'' +
                ", mArtist='" + mArtist + '\'' +
                ", mURL='" + mURL + '\'' +
                ", mLyricsID='" + mLyricsID + '\'' +
                '}';
    }
}
