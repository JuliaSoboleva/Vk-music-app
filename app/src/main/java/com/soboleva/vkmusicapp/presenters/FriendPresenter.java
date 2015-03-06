package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.soboleva.vkmusicapp.api.vk.callbacks.OnFriendListDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;
import com.soboleva.vkmusicapp.ui.fragments.FriendListFragment;
import timber.log.Timber;

import java.util.List;

public class FriendPresenter {

    private final FriendListFragment mFriendListFragment;
    private final VkApi mVkApi;
    private int mTotalFriendCount;
    private int mAvailableFriendCount;

    private boolean mIsDownloadingNow;

    public static final int PAGE_SIZE = 20;



    public FriendPresenter(FriendListFragment fragment) {
        mFriendListFragment = fragment;
        mVkApi = VkApi.getInstance();
        mTotalFriendCount = 0;
        mAvailableFriendCount = 0;
        mIsDownloadingNow = false;

    }

    public void getMyFriends() {
        mAvailableFriendCount = 0;
        getMyFriends(0, PAGE_SIZE);
    }


    public void getMyFriends(final int offset, int count) {
        mIsDownloadingNow = true;
        mVkApi.getFriends(new OnFriendListDownloadedListener() {
            @Override
            public void onFriendListDownloaded(List<Friend> friends, int totalCount) {
                mAvailableFriendCount += friends.size();
                Timber.d("mAvailableFriendCount = %d, mTotalFriendCount = %d", mAvailableFriendCount, mTotalFriendCount);
                if (offset == 0) {
                    mFriendListFragment.showFriends(friends);
                    mTotalFriendCount = totalCount;
                } else {
                    mFriendListFragment.showWithAddedFriends(friends);
                }
                mIsDownloadingNow = false;
            }

            @Override
            public void onError() {
                Timber.d("getFriends Error");
                mIsDownloadingNow = false;
            }
        }, offset, count);

    }

    public int getTotalFriendCount() {
        return mTotalFriendCount;
    }

    public int getAvailableFriendCount() {
        return mAvailableFriendCount;
    }

    public boolean isDownloadingNow() {
        return mIsDownloadingNow;
    }
}
