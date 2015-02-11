package com.soboleva.vkmusicapp.api.vk.callbacks;

public interface OnAudioDownloadedListener {
    public void onAudioDownloaded(String title);

    public void onError();
}
