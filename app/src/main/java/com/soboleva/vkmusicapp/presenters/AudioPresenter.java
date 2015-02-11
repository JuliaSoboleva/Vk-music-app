package com.soboleva.vkmusicapp.presenters;

import android.content.Intent;
import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.soboleva.vkmusicapp.api.vk.callbacks.OnAudioDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.callbacks.OnAudioListDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.models.Audio;
import com.soboleva.vkmusicapp.ui.activities.AudioListActivity;
import timber.log.Timber;

import java.util.List;

public class AudioPresenter {

    private final AudioListActivity mAudioActivity;
    private final VkApi mVkApi;

    public AudioPresenter(AudioListActivity activity) {
        mAudioActivity = activity;
        mVkApi = VkApi.getInstance();
    }

    public void onActivityResult(AudioListActivity audioListActivity, int requestCode, int resultCode, Intent data) {
        mVkApi.onActivityResult(audioListActivity, requestCode, resultCode, data);
    }

    public void getMyAudio() {
        mVkApi.getMyAudio(new OnAudioListDownloadedListener() {
            @Override
            public void onMusicListDownloaded(List<Audio> audios) {
                mAudioActivity.showAudio(audios);
            }

            @Override
            public void onError() {
                Timber.d("getMyAudio Error");
            }
        });

    }

    public void getSearchedAudio(String searchedRequest) {
        mVkApi.getSearchedAudio(new OnAudioListDownloadedListener() {
            @Override
            public void onMusicListDownloaded(List<Audio> audios) {
                mAudioActivity.showAudio(audios);
            }

            @Override
            public void onError() {
                Timber.d("getSearchedAudio Error");
            }
        }, searchedRequest);
    }

    public void getMyAudio(int offset, int count) {

    }

    public void downloadAudio(Audio audio) {
        mVkApi.downloadAudio(new OnAudioDownloadedListener() {
            @Override
            public void onAudioDownloaded(String title) {
                mAudioActivity.sayAudioDownloaded(title);
            }

            @Override
            public void onError() {
                Timber.d("downloadAudio Error");
            }
        }, audio);

    }


}

