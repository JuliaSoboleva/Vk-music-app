package com.soboleva.vkmusicapp.api.vk.models.audios;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AudioResponse {

    @SerializedName("count")
    private int mCount;

    @SerializedName("items")
    private List<Audio> mAudioList;

    public List<Audio> getAudioList() {
        return mAudioList;
    }

    public int getTotalCount() {
        return mCount;
    }

    @Override
    public String toString() {
        return "AudioResponse{" +
                "mAudioList=" + mAudioList +
                '}';
    }
}
