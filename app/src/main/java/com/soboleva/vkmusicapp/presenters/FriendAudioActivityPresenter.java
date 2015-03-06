package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.soboleva.vkmusicapp.api.vk.callbacks.OnAudioListDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;
import com.soboleva.vkmusicapp.ui.activities.FriendAudioActivity;
import timber.log.Timber;

import java.util.List;

public class FriendAudioActivityPresenter {
    private final FriendAudioActivity mFriendAudioActivity;
    private final VkApi mVkApi;
    private int mTotalAudioCount;
    private int mAvailableAudioCount;
    private boolean mIsDownloadingNow;

    private Friend mFriend;



    public static final int PAGE_SIZE = 20;

    public FriendAudioActivityPresenter(FriendAudioActivity friendAudioActivity, Friend friend) {
        mFriendAudioActivity = friendAudioActivity;
        mFriend = friend;
        mVkApi = VkApi.getInstance();
        mTotalAudioCount = 0;
        mAvailableAudioCount = 0;
        mIsDownloadingNow = false;
    }

    public void getAudio() {
        mAvailableAudioCount = 0;
        getAudio(0, PAGE_SIZE);
    }


    public void getAudio(final int offset, int count) {
        mIsDownloadingNow = true;
        mVkApi.getAudio(new OnAudioListDownloadedListener() {
            @Override
            public void onAudioListDownloaded(List<Audio> audios, int totalCount) {
                mAvailableAudioCount += audios.size();
                if (offset == 0) {
                    mFriendAudioActivity.showAudio(audios);
                    mTotalAudioCount = totalCount;
                } else {
                    mFriendAudioActivity.showWithAddedAudio(audios);
                }
                mIsDownloadingNow = false;
            }

            @Override
            public void onError() {
                Timber.d("getMyAudio Error");
                mIsDownloadingNow = false;
            }
        }, offset, count, Integer.parseInt(mFriend.getID()));

    }
}
