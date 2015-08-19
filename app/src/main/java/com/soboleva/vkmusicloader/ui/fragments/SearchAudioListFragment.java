package com.soboleva.vkmusicloader.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.soboleva.vkmusicloader.vk.callbacks.OnAddButtonClickListener;
import com.soboleva.vkmusicloader.vk.callbacks.OnDownloadButtonClickListener;
import com.soboleva.vkmusicloader.vk.models.audios.Audio;
import com.soboleva.vkmusicloader.presenters.SearchAudioPresenter;
import com.soboleva.vkmusicloader.ui.adapters.AudioListAdapter;

public class SearchAudioListFragment extends AudioListFragment {

    public static String REQUEST_KEY = "request";

    // instanceOf constructor for creating fragment with arguments
    public static Fragment instanceOf(Context context, String request) {
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

        setListAdapter(new AudioListAdapter(new OnDownloadButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                downloadAudio(audio);
            }
        }, new OnAddButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                addAudio(audio);
                audio.setAdded(true);
            }
        }) );

        //setScrollListener();


    }



}
