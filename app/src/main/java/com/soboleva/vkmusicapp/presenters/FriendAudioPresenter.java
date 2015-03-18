package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.callbacks.OnAudioListDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;
import com.soboleva.vkmusicapp.ui.fragments.BaseListFragment;
import timber.log.Timber;

import java.util.List;

public class FriendAudioPresenter extends AudioPresenter {

    private Friend mFriend;

    public FriendAudioPresenter(BaseListFragment fragment, Friend friend) {
        super(fragment);
        mFriend = friend;
    }

    @Override
    public void getItems(final int offset, int count) {
        mIsDownloadingNow = true;
        mVkApi.getAudio(new OnAudioListDownloadedListener() {
            @Override
            public void onAudioListDownloaded(List<Audio> audios, int totalCount) {
                mAvailableItemCount += audios.size();
                Timber.d("mAvailableAudioCount = %d, mTotalAudioCount = %d", mAvailableItemCount, mTotalItemCount);
                if (offset == 0) {
                    mBaseListFragment.showItems(audios);
                    mTotalItemCount = totalCount;
                } else {
                    mBaseListFragment.showWithAddedItems(audios);
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
