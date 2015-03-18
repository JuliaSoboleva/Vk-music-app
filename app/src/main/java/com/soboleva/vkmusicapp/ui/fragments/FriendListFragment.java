package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;
import com.soboleva.vkmusicapp.presenters.FriendPresenter;
import com.soboleva.vkmusicapp.presenters.OwnAudioPresenter;
import com.soboleva.vkmusicapp.ui.activities.FriendAudioActivity;
import com.soboleva.vkmusicapp.ui.adapters.FriendListAdapter;
import timber.log.Timber;

import java.util.List;

public class FriendListFragment extends ListFragment {

    private FriendPresenter mFriendPresenter;


    // newInstance constructor for creating fragment with arguments
    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context, FriendListFragment.class.getName());
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriendPresenter = new FriendPresenter(this);
        mFriendPresenter.getMyFriends();

        setListAdapter(new FriendListAdapter(getActivity()));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //noop
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int total = mFriendPresenter.getTotalFriendCount();
                int available = mFriendPresenter.getAvailableFriendCount();
                if (firstVisibleItem + visibleItemCount >= totalItemCount - 1 && available < total && !mFriendPresenter.isDownloadingNow()) {
                    if (total - available >= OwnAudioPresenter.PAGE_SIZE) {
                        mFriendPresenter.getMyFriends(available, OwnAudioPresenter.PAGE_SIZE);
                    } else {
                        mFriendPresenter.getMyFriends(available, total - available);
                    }
                    Timber.d("need to refresh friendList, totalItemCount = %d ", totalItemCount);
                }
            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = (Friend) getListAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), FriendAudioActivity.class);
                intent.putExtra(FriendAudioActivity.FRIEND, friend);
                startActivity(intent);
            }
        });
    }

    public void showFriends(List<Friend> friendList) {
        FriendListAdapter adapter = (FriendListAdapter) getListAdapter();
        adapter.setFriendList(friendList);

    }

    public void showWithAddedFriends(List<Friend> addedFriendList) {
        ((FriendListAdapter) getListAdapter()).setAddedFriendList(addedFriendList);

    }

}
