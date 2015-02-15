package com.soboleva.vkmusicapp.api.vk.callbacks;

import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;

import java.util.List;

public interface OnAudioListDownloadedListener {
    public void onAudioListDownloaded(List<Audio> audios, int totalCount);

    public void onError();
}
