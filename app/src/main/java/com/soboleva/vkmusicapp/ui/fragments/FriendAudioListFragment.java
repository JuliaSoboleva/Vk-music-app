package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;
import com.soboleva.vkmusicapp.presenters.FriendAudioPresenter;
import com.soboleva.vkmusicapp.ui.activities.FriendAudioActivity;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;

public class FriendAudioListFragment extends AudioListFragment {

    public static Fragment instantiate(Context context, Friend friend) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(FriendAudioActivity.FRIEND, friend);
        return Fragment.instantiate(context, FriendAudioListFragment.class.getName(), bundle);
    }


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaseListPresenter = new FriendAudioPresenter(this, (Friend)getArguments().getSerializable(FriendAudioActivity.FRIEND));
        mBaseListPresenter.getItems();

        setListAdapter(new AudioListAdapter(new AudioListAdapter.OnDownloadButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                downloadAudio(audio);
            }
        }, new AudioListAdapter.OnAddButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                addAudio(audio);
            }
        }));

        //setScrollListener();



    }


}
