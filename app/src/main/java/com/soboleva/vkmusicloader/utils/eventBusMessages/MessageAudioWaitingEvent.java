package com.soboleva.vkmusicloader.utils.eventBusMessages;

public class MessageAudioWaitingEvent {
    private final String mAudioID;
    private final boolean mIsWaiting;

    public MessageAudioWaitingEvent(String audioID, boolean isWaiting) {
        this.mAudioID = audioID;
        this.mIsWaiting = isWaiting;
    }

    public String getAudioID() {
        return mAudioID;
    }

    public boolean isWaiting() {
        return mIsWaiting;
    }
}
