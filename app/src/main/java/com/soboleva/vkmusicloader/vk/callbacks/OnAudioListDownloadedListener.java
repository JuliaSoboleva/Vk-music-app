package com.soboleva.vkmusicloader.vk.callbacks;

import com.soboleva.vkmusicloader.vk.models.audios.Audio;

import java.util.List;

public interface OnAudioListDownloadedListener {
    public void onAudioListDownloaded(List<Audio> audios, int totalCount);

    public void onError();
}
