package com.soboleva.vkmusicapp.presenters;

import android.content.Intent;
import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.soboleva.vkmusicapp.api.vk.callbacks.OnAudioListDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.ui.activities.AudioListActivity;
import timber.log.Timber;

import java.util.List;

public class AudioPresenter {

    private final AudioListActivity mAudioActivity;
    private final VkApi mVkApi;
    private int mTotalAudioCount;
    private int mAvailableAudioCount;
    private boolean mIsDownloadingNow;

    public static final int PAGE_SIZE = 20;

    public AudioPresenter(AudioListActivity activity) {
        mAudioActivity = activity;
        mVkApi = VkApi.getInstance();
        mTotalAudioCount = 0;
        mAvailableAudioCount = 0;
        mIsDownloadingNow = false;

    }

    public void onActivityResult(AudioListActivity audioListActivity, int requestCode, int resultCode, Intent data) {
        mVkApi.onActivityResult(audioListActivity, requestCode, resultCode, data);
    }

    public void getMyAudio() {
        mAvailableAudioCount = 0;
        getMyAudio(0, PAGE_SIZE);
    }


    public void getMyAudio(final int offset, int count) {
        mIsDownloadingNow = true;
        mVkApi.getMyAudio(new OnAudioListDownloadedListener() {
            @Override
            public void onAudioListDownloaded(List<Audio> audios, int totalCount) {
                mAvailableAudioCount += audios.size();
                Timber.d("mAvailableAudioCount = %d, mTotalAudioCount = %d", mAvailableAudioCount, mTotalAudioCount);
                if (offset == 0) {
                    mAudioActivity.showAudio(audios);
                    mTotalAudioCount = totalCount;
                } else {
                    mAudioActivity.showWithAddedAudio(audios);
                }
                mIsDownloadingNow = false;
            }

            @Override
            public void onError() {
                Timber.d("getMyAudio Error");
                mIsDownloadingNow = false;
            }
        }, offset, count);

    }

    public void getSearchedAudio(String searchedRequest) {
        mAvailableAudioCount = 0;
        getSearchedAudio(searchedRequest, 0, PAGE_SIZE);
    }

    public void getSearchedAudio(String searchedRequest, final int offset, int count) {
        mIsDownloadingNow = true;
        mVkApi.getSearchedAudio(new OnAudioListDownloadedListener() {
            @Override
            public void onAudioListDownloaded(List<Audio> audios, int totalCount) {
                mAvailableAudioCount += audios.size();
                if (offset == 0) {
                    mAudioActivity.showAudio(audios);
                    mTotalAudioCount = totalCount;

                } else {
                    mAudioActivity.showWithAddedAudio(audios);
                }
                mIsDownloadingNow = false;
            }

            @Override
            public void onError() {
                Timber.d("getSearchedAudio Error");
                mIsDownloadingNow = false;
            }
        }, searchedRequest, offset, count);
    }

    public int getTotalAudioCount() {
        return mTotalAudioCount;
    }

    public int getAvailableAudioCount() {
        return mAvailableAudioCount;
    }

    public boolean isDownloadingNow() {
        return mIsDownloadingNow;
    }
}

