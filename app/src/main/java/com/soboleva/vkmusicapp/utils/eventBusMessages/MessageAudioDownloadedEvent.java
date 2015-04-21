package com.soboleva.vkmusicapp.utils.eventBusMessages;

public class MessageAudioDownloadedEvent {
    //public final String message;
    private final String mAudioID;
    private final boolean mIsDownloading;

    public MessageAudioDownloadedEvent(String audioID, boolean isDownloading) {
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

