package com.soboleva.vkmusicloader.presenters;

import com.soboleva.vkmusicloader.vk.VkApi;
import com.soboleva.vkmusicloader.vk.models.BaseData;
import com.soboleva.vkmusicloader.ui.fragments.BaseListFragment;

import java.util.List;

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

    protected void showItems(int offset, List<? extends BaseData> data, int totalCount) {
        mAvailableItemCount += data.size();
        if (offset == 0) {
            mBaseListFragment.showItems(data);
            mTotalItemCount = totalCount;
        } else {
            mBaseListFragment.showWithAddedItems(data);
        }
        mIsDownloadingNow = false;
    }

    public BaseListFragment getBaseListFragment() {
        return mBaseListFragment;
    }


}
