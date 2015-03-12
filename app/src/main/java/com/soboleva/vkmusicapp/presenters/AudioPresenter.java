package com.soboleva.vkmusicapp.presenters;

import android.support.v4.app.Fragment;
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

    public AudioPresenter(Fragment fragment) {
        mAudioFragment = (AudioListFragment)fragment;
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
        mVkApi.addAudio(Integer.parseInt(audio.getID()), Integer.parseInt(audio.getOwnerID()));
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
