package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.callbacks.OnAudioListDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;
import com.soboleva.vkmusicapp.ui.fragments.AudioListFragment;
import timber.log.Timber;

import java.util.List;

public class FriendAudioPresenter extends AudioPresenter {

    private Friend mFriend;

    public FriendAudioPresenter(AudioListFragment fragment, Friend friend) {
        super(fragment);
        mFriend = friend;
    }

    @Override
    public void getAudio(final int offset, int count) {
        mIsDownloadingNow = true;
        mVkApi.getAudio(new OnAudioListDownloadedListener() {
            @Override
            public void onAudioListDownloaded(List<Audio> audios, int totalCount) {
                mAvailableAudioCount += audios.size();
                Timber.d("mAvailableAudioCount = %d, mTotalAudioCount = %d", mAvailableAudioCount, mTotalAudioCount);
                if (offset == 0) {
                    mAudioFragment.showAudio(audios);
                    mTotalAudioCount = totalCount;
                } else {
                    mAudioFragment.showWithAddedAudio(audios);
                }
                mIsDownloadingNow = false;
            }

            @Override
            public void onError() {
                Timber.d("getMyAudio Error");
                mIsDownloadingNow = false;
            }
        }, offset, count, mFriend.getID());

    }
}
