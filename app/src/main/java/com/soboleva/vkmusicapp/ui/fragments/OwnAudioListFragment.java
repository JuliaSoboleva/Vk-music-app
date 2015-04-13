package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.presenters.OwnAudioPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import timber.log.Timber;

public class OwnAudioListFragment extends AudioListFragment {

    // instanceOf constructor for creating fragment with arguments
    public static Fragment instantiate(Context context) {
        return Fragment.instantiate(context, OwnAudioListFragment.class.getName());
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


}

