package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import com.soboleva.vkmusicapp.api.vk.models.BaseData;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;
import com.soboleva.vkmusicapp.presenters.FriendPresenter;
import com.soboleva.vkmusicapp.ui.activities.FriendAudioActivity;
import com.soboleva.vkmusicapp.ui.adapters.FriendListAdapter;

import java.util.List;

public class FriendListFragment extends BaseListFragment {


    // newInstance constructor for creating fragment with arguments
    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context, FriendListFragment.class.getName());
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseListPresenter = new FriendPresenter(this);
        mBaseListPresenter.getItems();

        setListAdapter(new FriendListAdapter(getActivity()));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    @Override
    public void showItems(List<? extends BaseData> dataList) {
        FriendListAdapter adapter = (FriendListAdapter) getListAdapter();
        adapter.setFriendList((List<Friend>)dataList);
    }

    @Override
    public void showWithAddedItems(List<? extends BaseData> dataList) {
        FriendListAdapter adapter = (FriendListAdapter) getListAdapter();
        adapter.setAddedFriendList((List<Friend>)dataList);
    }

}
