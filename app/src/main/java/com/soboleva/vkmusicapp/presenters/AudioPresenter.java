package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.ui.fragments.AudioListFragment;

public abstract class AudioPresenter {

    protected final AudioListFragment mAudioFragment;
    protected final VkApi mVkApi;
    protected int mTotalAudioCount;
    protected int mAvailableAudioCount;
    protected boolean mIsDownloadingNow;

    public static final int PAGE_SIZE = 20;

    public AudioPresenter(AudioListFragment fragment) {
        mAudioFragment = fragment;
        mVkApi = VkApi.getInstance();
        mTotalAudioCount = 0;
        mAvailableAudioCount = 0;
        mIsDownloadingNow = false;
    }

    public void getAudio() {
        mAvailableAudioCount = 0;
        getAudio(0, PAGE_SIZE);
    }

    public void addAudio(Audio audio) {
        mVkApi.addAudio(audio.getID(), audio.getOwnerID());
    }

    public abstract void getAudio(int offset, int count);

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
