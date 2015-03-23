package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.soboleva.vkmusicapp.AudioIntentService;
import com.soboleva.vkmusicapp.api.vk.models.BaseData;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.presenters.AudioPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import timber.log.Timber;

import java.util.List;

public class AudioListFragment extends BaseListFragment {

    public static Fragment instantiate(Context context) {
        return Fragment.instantiate(context, AudioListFragment.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void downloadAudio(Audio audio) {
        Timber.d("Downloading audio %s - %s", audio.getArtist(), audio.getTitle());
        Intent i = new Intent(getActivity().getApplicationContext(), AudioIntentService.class);
        i.putExtra(AudioIntentService.PARAM_URL, audio.getURL());
        i.putExtra(AudioIntentService.PARAM_TITLE, audio.getTitle());
        i.putExtra(AudioIntentService.PARAM_ARTIST, audio.getArtist());
        getActivity().getApplicationContext().startService(i);
    }

    public void addAudio(Audio audio) {
        AudioPresenter audioPresenter = (AudioPresenter)mBaseListPresenter;
        audioPresenter.addAudio(audio);
    }


    @Override
    public void showItems(List<? extends BaseData> dataList) {
        AudioListAdapter adapter = (AudioListAdapter)getListAdapter();
        //List<Audio> audios = dataList;
        adapter.setAudioList((List<Audio>)dataList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showWithAddedItems(List<? extends BaseData> dataList) {
        AudioListAdapter adapter = (AudioListAdapter)getListAdapter();
        adapter.setAddedAudioList((List<Audio>)dataList);
        adapter.notifyDataSetChanged();
    }
}
