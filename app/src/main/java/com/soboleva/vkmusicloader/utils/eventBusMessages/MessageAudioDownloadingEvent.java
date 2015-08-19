package com.soboleva.vkmusicloader.utils.eventBusMessages;

public class MessageAudioDownloadingEvent {
    //public final String message;
    private final String mAudioID;
    private final boolean mIsDownloading;

    public MessageAudioDownloadingEvent(String audioID, boolean isDownloading) {
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

