package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.presenters.SearchAudioPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;

public class SearchAudioListFragment extends AudioListFragment {

    public static String REQUEST_KEY = "request";

    // instantiate constructor for creating fragment with arguments
    public static Fragment newInstance(Context context, String request) {
        Bundle bundle = new Bundle();
        bundle.putString(REQUEST_KEY, request);
        return Fragment.instantiate(context, SearchAudioListFragment.class.getName(), bundle);
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        mBaseListPresenter = new SearchAudioPresenter(this, bundle.getString(REQUEST_KEY));
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
        }) );

        //setScrollListener();


    }


}
