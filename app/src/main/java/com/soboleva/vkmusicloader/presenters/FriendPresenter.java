package com.soboleva.vkmusicloader.presenters;

import com.soboleva.vkmusicloader.vk.callbacks.OnFriendListDownloadedListener;
import com.soboleva.vkmusicloader.vk.models.friends.Friend;
import com.soboleva.vkmusicloader.ui.fragments.BaseListFragment;
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
                if (friends.size()==0) {
                    mBaseListFragment.showMessage(BaseListFragment.STATE_NO_FRIENDS);
                } else {
                    showItems(offset, friends, totalCount);
                }
            }

            @Override
            public void onError() {
                Timber.d("getFriends Error");
                mIsDownloadingNow = false;
                mBaseListFragment.showMessage(BaseListFragment.STATE_ERROR);
            }
        }, offset, count);

    }

}
