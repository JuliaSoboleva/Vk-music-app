package com.soboleva.vkmusicapp.api.vk.models.audios;

import com.google.gson.annotations.SerializedName;
import com.soboleva.vkmusicapp.api.vk.models.BaseData;

public class Audio extends BaseData {

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

    private boolean mIsDownloading;
    private boolean mIsAdded;
    private int mDownloadingProgress;
    private boolean mIsWaiting;

    //private boolean mIsStateChanged;


//    идентификатор жанра
//    @SerializedName("genre_id")
//    private String mGenreID;

//    @SerializedName("album_id")
//    private String mAlbumID;


    //todo getDuration without player

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

    public boolean isDownloading() {
        return mIsDownloading;
    }

    public void setDownloading(boolean isDownloading) {
        mIsDownloading = isDownloading;
        if(isDownloading) {
            mIsWaiting = false;
        }
    }

    public boolean isAdded() {
        return mIsAdded;
    }

    public void setAdded(boolean isAdded) {
        mIsAdded = isAdded;
    }

    public int getDownloadingProgress() {
        return mDownloadingProgress;
    }

    public void setDownloadingProgress(int downloadingProgress) {
        mDownloadingProgress = downloadingProgress;
    }

    public boolean isWaiting() {
        return mIsWaiting;
    }

    public void setWaiting(boolean isWaiting) {
        mIsWaiting = isWaiting;
        if (mIsWaiting) {
            mIsDownloading = false;
        }
    }


}
