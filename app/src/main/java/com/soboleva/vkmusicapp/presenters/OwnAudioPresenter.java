package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.callbacks.OnAudioListDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import com.soboleva.vkmusicapp.ui.fragments.BaseListFragment;
import com.soboleva.vkmusicapp.utils.MessageEvent;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

import java.util.List;

public class OwnAudioPresenter extends AudioPresenter{


    public OwnAudioPresenter(BaseListFragment fragment) {
        super(fragment);
        EventBus.getDefault().register(this);
    }



    @Override
    public void getItems(final int offset, int count) {
        mIsDownloadingNow = true;
        mVkApi.getMyAudio(new OnAudioListDownloadedListener() {
            @Override
            public void onAudioListDownloaded(List<Audio> audios, int totalCount) {
                showItems(offset, audios, totalCount);
            }

            @Override
            public void onError() {
                Timber.d("getMyAudio Error");
                mIsDownloadingNow = false;
            }
        }, offset, count);

    }

    public void onEventMainThread(MessageEvent event){
        Timber.d("onEvent ownAudioPresenter works");
        AudioListAdapter adapter = (AudioListAdapter)getBaseListFragment().getListAdapter();
        adapter.changeAudioStates(event.getAudioID(), event.isDownloading());

    }

}

