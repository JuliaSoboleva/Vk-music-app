package com.soboleva.vkmusicapp.utils.eventBusMessages;

public class MessageDownloadingProgressEvent {
    private final String mAudioID;
    private final int mProgress;

    public MessageDownloadingProgressEvent(String audioID, int progress) {
        this.mAudioID = audioID;
        this.mProgress = progress;
    }

    public String getAudioID() {
        return mAudioID;
    }

    public int getProgress() {
        return mProgress;
    }
}
