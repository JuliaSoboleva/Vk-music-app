package com.soboleva.vkmusicapp.api.vk.callbacks;

import com.soboleva.vkmusicapp.api.vk.models.Audio;

import java.util.List;

public interface OnAudioListDownloadedListener {
    public void onMusicListDownloaded(List<Audio> audios);

    public void onError();
}
