package com.soboleva.vkmusicloader.presenters;

import com.soboleva.vkmusicloader.vk.callbacks.OnAudioListDownloadedListener;
import com.soboleva.vkmusicloader.vk.models.audios.Audio;
import com.soboleva.vkmusicloader.vk.models.friends.Friend;
import com.soboleva.vkmusicloader.ui.activities.FriendAudioActivity;
import com.soboleva.vkmusicloader.ui.fragments.BaseListFragment;
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
                Timber.d("Friend's audios: totalCount = %d ", totalCount);
                if(totalCount == 0) {
                    Timber.d("Friend has not audios");
                    mBaseListFragment.showMessage(BaseListFragment.STATE_NO_AUDIO);
                    if (mBaseListFragment.getActivity() != null) {
                        ((FriendAudioActivity) (mBaseListFragment.getActivity())).hidePhoto();
                    }
                } else {
                    showItems(offset, audios, totalCount);
                }
            }

            @Override
            public void onError() {
                Timber.d("getMyAudio Error");
                mIsDownloadingNow = false;
                mBaseListFragment.showMessage(BaseListFragment.STATE_NO_ACCESS);
                if (mBaseListFragment.getActivity() != null) {
                    ((FriendAudioActivity) (mBaseListFragment.getActivity())).hidePhoto();
                }
            }
        }, offset, count, mFriend.getID());

    }
}
