package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AbsListView;
import com.soboleva.vkmusicapp.AudioIntentService;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.presenters.AudioPresenter;
import com.soboleva.vkmusicapp.presenters.OwnAudioPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import timber.log.Timber;

import java.util.List;

public class AudioListFragment extends ListFragment {

//    protected ListView mAudioListView;
    protected AudioPresenter mAudioPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setScrollListener();
    }

    protected void setScrollListener() {
        this.getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //noop
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int total = mAudioPresenter.getTotalAudioCount();
                int available = mAudioPresenter.getAvailableAudioCount();
                if (firstVisibleItem + visibleItemCount >= totalItemCount - 1 && available < total && !mAudioPresenter.isDownloadingNow()) {
                    if (total - available >= OwnAudioPresenter.PAGE_SIZE) {
                        mAudioPresenter.getAudio(available, AudioPresenter.PAGE_SIZE);
                    } else {
                        mAudioPresenter.getAudio(available, total - available);
                    }
                    Timber.d("need to refresh audioList, totalItemCount = %d ", totalItemCount);
                }
            }
        });
    }

    public void downloadAudio(Audio audio) {
        Timber.d("Downloading audio %s - %s", audio.getArtist(), audio.getTitle());
        Intent i = new Intent(getActivity().getApplicationContext(), AudioIntentService.class);
        i.putExtra(AudioIntentService.URL, audio.getURL());
        i.putExtra(AudioIntentService.TITLE, audio.getTitle());
        i.putExtra(AudioIntentService.ARTIST, audio.getArtist());
        getActivity().getApplicationContext().startService(i);
    }

    public void addAudio(Audio audio) {
        mAudioPresenter.addAudio(audio);
    }


    public void showAudio(List<Audio> audioList) {
        AudioListAdapter adapter = (AudioListAdapter)getListAdapter();
        adapter.setAudioList(audioList);
        adapter.notifyDataSetChanged();
    }

    public void showWithAddedAudio(List<Audio> addedAudioList) {
        AudioListAdapter adapter = (AudioListAdapter)getListAdapter();
        adapter.setAddedAudioList(addedAudioList);
        adapter.notifyDataSetChanged();
    }
}
