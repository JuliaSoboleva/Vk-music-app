package com.soboleva.vkmusicloader.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.soboleva.vkmusicloader.AudioIntentService;
import com.soboleva.vkmusicloader.vk.models.BaseData;
import com.soboleva.vkmusicloader.vk.models.audios.Audio;
import com.soboleva.vkmusicloader.presenters.AudioPresenter;
import com.soboleva.vkmusicloader.ui.adapters.AudioListAdapter;
import com.soboleva.vkmusicloader.utils.eventBusMessages.MessageInterruptDownloadingEvent;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

import java.util.List;

public class AudioListFragment extends BaseListFragment {


//    private ImageView mEmptyImageView;
//    private TextView mEmptyTextView;
//    private View mEmptyView;


    public static Fragment instantiate(Context context) {
        return Fragment.instantiate(context, AudioListFragment.class.getName());
    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void downloadAudio(Audio audio) {
        if (audio.isDownloading()) {
            EventBus.getDefault().post(new MessageInterruptDownloadingEvent(audio.getID()));
            Timber.d("Interrupt downloading");
        } else {
            Timber.d("Downloading audio %s - %s", audio.getArtist(), audio.getTitle());
            Intent i = new Intent(getActivity().getApplicationContext(), AudioIntentService.class);
            i.putExtra(AudioIntentService.PARAM_URL, audio.getURL());
            i.putExtra(AudioIntentService.PARAM_TITLE, audio.getTitle());
            i.putExtra(AudioIntentService.PARAM_ARTIST, audio.getArtist());
            i.putExtra(AudioIntentService.PARAM_AUDIO_ID, audio.getID());
            i.putExtra(AudioIntentService.PARAM_DURATION, Integer.valueOf(audio.getDuration()));
            getActivity().getApplicationContext().startService(i);
        }
    }

    public void addAudio(Audio audio) {
        AudioPresenter audioPresenter = (AudioPresenter) mBaseListPresenter;
        audioPresenter.addAudio(audio);
    }

    @Override
    public void showItems(List<? extends BaseData> dataList) {
        AudioListAdapter adapter = (AudioListAdapter) getListAdapter();
        //List<Audio> audios = dataList;
        adapter.setAudioList((List<Audio>) dataList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showWithAddedItems(List<? extends BaseData> dataList) {
        AudioListAdapter adapter = (AudioListAdapter) getListAdapter();
        adapter.setAddedAudioList((List<Audio>) dataList);
        adapter.notifyDataSetChanged();
    }






}
