package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.soboleva.vkmusicapp.ui.fragments.BaseListFragment;

public abstract class BaseListPresenter {

    protected final BaseListFragment mBaseListFragment;
    protected final VkApi mVkApi;
    protected int mTotalItemCount;
    protected int mAvailableItemCount;

    protected boolean mIsDownloadingNow;

    public static final int PAGE_SIZE = 20;

    public BaseListPresenter(BaseListFragment baseListFragment) {
        mBaseListFragment = baseListFragment;
        mVkApi = VkApi.getInstance();
        mTotalItemCount = 0;
        mAvailableItemCount = 0;
        mIsDownloadingNow = false;
    }

    public void getItems() {
        mAvailableItemCount = 0;
        getItems(0, PAGE_SIZE);
    }

    public abstract void getItems(int offset, int count);


    public int getTotalItemCount() {
        return mTotalItemCount;
    }

    public int getAvailableItemCount() {
        return mAvailableItemCount;
    }

    public boolean isDownloadingNow() {
        return mIsDownloadingNow;
    }
}
