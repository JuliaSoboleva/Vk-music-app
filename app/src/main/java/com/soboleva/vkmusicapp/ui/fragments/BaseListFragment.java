package com.soboleva.vkmusicapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AbsListView;
import com.soboleva.vkmusicapp.api.vk.models.BaseData;
import com.soboleva.vkmusicapp.presenters.AudioPresenter;
import com.soboleva.vkmusicapp.presenters.BaseListPresenter;
import timber.log.Timber;

import java.util.List;

public abstract class BaseListFragment extends ListFragment {

    public static String STATE_KEY = "state";
    public static final int STATE_NO_AUDIO = 0;
    public static final int STATE_NO_INTERNET = 1;
    public static final int STATE_ERROR = 2;

    protected BaseListPresenter mBaseListPresenter;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        setScrollListener();



    }

    private void setScrollListener() {
        this.getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //noop
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int total = mBaseListPresenter.getTotalItemCount();
                int available = mBaseListPresenter.getAvailableItemCount();
                if (firstVisibleItem + visibleItemCount >= totalItemCount - 1 && available < total && !mBaseListPresenter.isDownloadingNow()) {
                    if (total - available >= BaseListPresenter.PAGE_SIZE) {
                        mBaseListPresenter.getItems(available, AudioPresenter.PAGE_SIZE);
                    } else {
                        mBaseListPresenter.getItems(available, total - available);
                    }
                    Timber.d("need to refresh List, totalItemCount = %d ", totalItemCount);
                }
            }
        });
    }
    public abstract void showItems(List<? extends BaseData> dataList);
    public abstract void showWithAddedItems(List<? extends BaseData> dataList);

}
