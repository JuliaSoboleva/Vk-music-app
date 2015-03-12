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
import com.soboleva.vkmusicapp.presenters.SearchAudioPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;

public class SearchAudioListFragment extends AudioListFragment {

    public static String REQUEST_KEY = "request";

    // newInstance constructor for creating fragment with arguments
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

        mAudioPresenter = new SearchAudioPresenter(this, bundle.getString(REQUEST_KEY));
        mAudioPresenter.getAudio();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_audio_list, container, false);
        mAudioListView = (ListView) view.findViewById(R.id.list_search_audio);

        //todo
        mAudioListView.setAdapter(new AudioListAdapter(new AudioListAdapter.OnDownloadButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                downloadAudio(audio);
            }
        }, new AudioListAdapter.OnAddButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                mAudioPresenter.addAudio(audio);
            }
        }) );



        return view;
    }

}
