package com.soboleva.vkmusicloader.presenters;

import com.soboleva.vkmusicloader.vk.callbacks.OnAudioListDownloadedListener;
import com.soboleva.vkmusicloader.vk.models.audios.Audio;
import com.soboleva.vkmusicloader.ui.fragments.AudioListFragment;
import com.soboleva.vkmusicloader.ui.fragments.BaseListFragment;
import timber.log.Timber;

import java.util.List;

public class OwnAudioPresenter extends AudioPresenter {


    public OwnAudioPresenter(BaseListFragment fragment) {
        super(fragment);

    }


    @Override
    public void getItems(final int offset, int count) {
        mIsDownloadingNow = true;
        mVkApi.getMyAudio(new OnAudioListDownloadedListener() {
            @Override
            public void onAudioListDownloaded(List<Audio> audios, int totalCount) {
                Timber.d("onAudioListDownloaded, totalCount = %d", totalCount);
                if (totalCount == 0) {
                    ((AudioListFragment) mBaseListFragment).showMessage(BaseListFragment.STATE_NO_AUDIO);
                } else {
                    showItems(offset, audios, totalCount);
                }
            }

            @Override
            public void onError() {
                Timber.d("getMyAudio Error");
                mIsDownloadingNow = false;
                mBaseListFragment.showMessage(BaseListFragment.STATE_ERROR);
            }
        }, offset, count);

    }




}

