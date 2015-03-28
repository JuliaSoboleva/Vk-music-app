package com.soboleva.vkmusicapp.utils;

public class MessageEvent {
    //public final String message;
    private final String mAudioID;
    private final boolean mIsDownloading;

    public MessageEvent(String audioID, boolean isDownloading) {
        this.mAudioID = audioID;
        this.mIsDownloading = isDownloading;
    }

    public String getAudioID() {
        return mAudioID;
    }

    public boolean isDownloading() {
        return mIsDownloading;
    }
}

