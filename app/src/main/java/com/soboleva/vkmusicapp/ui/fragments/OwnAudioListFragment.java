package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import com.soboleva.swipemenu.SwipeMenu;
import com.soboleva.swipemenu.SwipeMenuCreator;
import com.soboleva.swipemenu.SwipeMenuItem;
import com.soboleva.swipemenu.SwipeMenuListView;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.presenters.AudioPresenter;
import com.soboleva.vkmusicapp.presenters.BaseListPresenter;
import com.soboleva.vkmusicapp.presenters.OwnAudioPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import com.soboleva.vkmusicapp.utils.CustomSwipeToRefresh;
import timber.log.Timber;

public class OwnAudioListFragment extends AudioListFragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    SwipeMenuListView mSwipeMenuListView;

    // instanceOf constructor for creating fragment with arguments
    public static Fragment instantiate(Context context) {
        return Fragment.instantiate(context, OwnAudioListFragment.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_own_audio_list, container, false);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        //((EnhancedListView) getListView()).discardUndo();
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseListPresenter = new OwnAudioPresenter(this);
        mBaseListPresenter.getItems();

        setListAdapter(new AudioListAdapter(new AudioListAdapter.OnDownloadButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                Timber.d("on click OwnAudioListFragment");
                downloadAudio(audio);
            }
        }));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSwipeToRefresh();

        setScrollListener();

        setSwipeMenuListView();
    }


    private void setScrollListener() {
        this.getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //noop
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0);

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

    private void setSwipeToRefresh() {
        mSwipeRefreshLayout = (CustomSwipeToRefresh) getActivity().findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Timber.d("Refresh");
                // start showing progress
                mSwipeRefreshLayout.setRefreshing(true);
                // wait 3 sec and hide progress
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBaseListPresenter.getItems();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

            }
        });

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPink),
                getResources().getColor(R.color.colorDeepPurpure),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorPrimary));
    }

    private void setSwipeMenuListView() {

        mSwipeMenuListView = (SwipeMenuListView)getListView();
        mSwipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorPink)));
                // set item width
                deleteItem.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                        getActivity().getApplicationContext().getResources().getDisplayMetrics()));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_cancel_wth);

                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        mSwipeMenuListView.setMenuCreator(creator);


        ((SwipeMenuListView)getListView()).setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
               switch (index) {
                   case 0:
                       // delete
                       ((AudioPresenter)mBaseListPresenter).deleteAudio(position);
                       //((AudioListAdapter)getListAdapter()).remove(position);
                       break;
               }
               // false : close the menu; true : not close the menu
               return false;
           }
       });


    }
}

