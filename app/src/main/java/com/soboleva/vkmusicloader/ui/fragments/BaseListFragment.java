package com.soboleva.vkmusicloader.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.soboleva.vkmusicloader.R;
import com.soboleva.vkmusicloader.vk.models.BaseData;
import com.soboleva.vkmusicloader.presenters.AudioPresenter;
import com.soboleva.vkmusicloader.presenters.BaseListPresenter;
import com.soboleva.vkmusicloader.utils.NetworkHelper;
import com.soboleva.vkmusicloader.utils.eventBusMessages.MessageNeedToUpdateEvent;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

import java.util.List;

public abstract class BaseListFragment extends ListFragment {

    public static final int STATE_NO_AUDIO = 0;
    public static final int STATE_NO_INTERNET = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_NO_ACCESS = 3;
    public static final int STATE_NO_FRIENDS = 4;

    protected BaseListPresenter mBaseListPresenter;

    protected ImageView mEmptyImageView;
    protected TextView mEmptyTextView;
    protected View mEmptyView;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        setScrollListener();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        getListView().setVerticalScrollBarEnabled(false);

        //looks like sometimes onStart() is not caused, so...
        if (getListView().getEmptyView() == null) {
            getListView().setEmptyView(
                    noItems(getResources().getString(R.string.no_audio)));
        }
    }

    private void setScrollListener() {
        this.getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //noop
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //mFirstVisibleItem = firstVisibleItem;
                int total = mBaseListPresenter.getTotalItemCount();
                int available = mBaseListPresenter.getAvailableItemCount();
                if (firstVisibleItem + visibleItemCount >= totalItemCount - 5 && available < total && !mBaseListPresenter.isDownloadingNow()) {
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

    public void showMessage(int stateID) {
        if (mEmptyImageView == null) {
           //return;
            getListView().setEmptyView(
                    noItems(getResources().getString(R.string.no_audio)));
        }
        mEmptyImageView.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.VISIBLE);
        switch (stateID) {
            case STATE_NO_AUDIO:
                mEmptyImageView.setImageResource(R.drawable.ic_owl_headphones);
                mEmptyTextView.setText(R.string.no_audio);
                break;
            case STATE_NO_INTERNET:
                mEmptyImageView.setImageResource(R.drawable.ic_owl);
                mEmptyTextView.setText(R.string.no_internet);

                break;
            case STATE_NO_ACCESS:
                mEmptyImageView.setImageResource(R.drawable.ic_owl);
                mEmptyTextView.setText(R.string.no_access);
                //todo
                break;
            case STATE_ERROR:
                if (this.getActivity()!=null && !NetworkHelper.isNetworkAvailable(this.getActivity().getApplicationContext())) {
                    mEmptyImageView.setImageResource(R.drawable.ic_owl);
                    mEmptyTextView.setText(R.string.no_internet);
                }
                else {
                    mEmptyImageView.setImageResource(R.drawable.ic_pug);
                    mEmptyTextView.setText(R.string.error);
                }
                break;
            case STATE_NO_FRIENDS:
                if (this.getActivity()!=null && !NetworkHelper.isNetworkAvailable(this.getActivity().getApplicationContext())) {
                    mEmptyImageView.setImageResource(R.drawable.ic_owl);
                    mEmptyTextView.setText(R.string.no_internet);
                }
                else {
                    mEmptyImageView.setImageResource(R.drawable.ic_pug);
                    mEmptyTextView.setText(R.string.no_friends);
                }
                break;
            default:
                break;

        }


    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setEmptyView(
                noItems(getResources().getString(R.string.no_audio)));

    }

    private View noItems(String text) {

        mEmptyView = getActivity().getLayoutInflater().inflate(R.layout.view_empty_list, getListView(), false);

        mEmptyImageView = (ImageView) mEmptyView.findViewById(R.id.picture_empty_list);
        mEmptyTextView = (TextView) mEmptyView.findViewById(R.id.text_empty_list);

        mEmptyImageView.setVisibility(View.INVISIBLE);
        mEmptyTextView.setVisibility(View.INVISIBLE);

        ((ViewGroup) getListView().getParent()).addView(mEmptyView, 0);
        return mEmptyView;
    }


    public void onEventMainThread(MessageNeedToUpdateEvent event){
        mBaseListPresenter.getItems();
    }


}
