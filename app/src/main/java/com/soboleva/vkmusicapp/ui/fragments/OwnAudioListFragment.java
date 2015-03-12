package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.presenters.OwnAudioPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;

public class OwnAudioListFragment extends AudioListFragment {

    // newInstance constructor for creating fragment with arguments
    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context, OwnAudioListFragment.class.getName());
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioPresenter = new OwnAudioPresenter(this);
        mAudioPresenter.getAudio();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_list, container, false);
        mAudioListView = (ListView) view.findViewById(R.id.audio_list);

        //todo
        mAudioListView.setAdapter(new AudioListAdapter(new AudioListAdapter.OnDownloadButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                downloadAudio(audio);
            }
        }));



        return view;
    }

    /*public void search(String query) {
        //todo
        mLastQuery = query;
        if (query.isEmpty()) {
            mIsMyAudio = true;
            mAudioPresenter.getMyAudio();
        } else {
            mIsMyAudio = false;
            mAudioPresenter.getSearchedAudio(query);
        }
        mAudioListView.smoothScrollToPosition(0);
    }*/


}

