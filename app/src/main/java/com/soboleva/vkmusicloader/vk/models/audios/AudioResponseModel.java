package com.soboleva.vkmusicloader.vk.models.audios;

import com.google.gson.annotations.SerializedName;

public class AudioResponseModel {

    @SerializedName("response")
    private AudioResponse mAudioResponse;

    public AudioResponse getAudioResponse() {
        return mAudioResponse;
    }

    @Override
    public String toString() {
        return "AudioResponseModel{" +
                "mAudioResponse=" + mAudioResponse +
                '}';
    }
}
