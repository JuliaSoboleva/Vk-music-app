package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.callbacks.OnFriendListDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;
import com.soboleva.vkmusicapp.ui.fragments.BaseListFragment;
import timber.log.Timber;

import java.util.List;

public class FriendPresenter extends BaseListPresenter {

    public FriendPresenter(BaseListFragment baseListFragment) {
        super(baseListFragment);
    }

    public void getItems(final int offset, int count) {
        mIsDownloadingNow = true;
        mVkApi.getFriends(new OnFriendListDownloadedListener() {
            @Override
            public void onFriendListDownloaded(List<Friend> friends, int totalCount) {
                showItems(offset, friends, totalCount);
            }

            @Override
            public void onError() {
                Timber.d("getFriends Error");
                mIsDownloadingNow = false;
            }
        }, offset, count);

    }

}
