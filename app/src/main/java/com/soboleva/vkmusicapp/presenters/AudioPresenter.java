package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import com.soboleva.vkmusicapp.ui.fragments.BaseListFragment;
import com.soboleva.vkmusicapp.utils.eventBusMessages.MessageAudioDownloadedEvent;
import com.soboleva.vkmusicapp.utils.eventBusMessages.MessageAudioWaitingEvent;
import com.soboleva.vkmusicapp.utils.eventBusMessages.MessageDownloadingProgressEvent;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

public abstract class AudioPresenter extends BaseListPresenter {


    protected AudioPresenter(BaseListFragment baseListFragment) {
        super(baseListFragment);
        EventBus.getDefault().register(this);
    }

    public void addAudio(Audio audio) {
        Timber.d("Adding audio %s", audio.getTitle());
        mVkApi.addAudio(audio.getID(), audio.getOwnerID());
        AudioListAdapter adapter = (AudioListAdapter)getBaseListFragment().getListAdapter();
        adapter.notifyDataSetChanged();
    }

    public void deleteAudio(int position){
        AudioListAdapter adapter = (AudioListAdapter)getBaseListFragment().getListAdapter();
        Audio audio = (Audio) adapter.getItem(position);
        adapter.remove(position);
        mVkApi.deleteAudio(audio.getID(), audio.getOwnerID());
        adapter.notifyDataSetChanged();
    }

    public void onEventMainThread(MessageAudioDownloadedEvent event){
        AudioListAdapter adapter = (AudioListAdapter)getBaseListFragment().getListAdapter();
        adapter.changeAudioStates(event.getAudioID(), AudioListAdapter.STATE_DOWNLOADING, event.isDownloading());
    }

    public void onEventMainThread(MessageDownloadingProgressEvent event){
        AudioListAdapter adapter = (AudioListAdapter)getBaseListFragment().getListAdapter();
        adapter.changeProgress(event.getAudioID(), event.getProgress());
    }

    public void onEventMainThread(MessageAudioWaitingEvent event) {
        AudioListAdapter adapter = (AudioListAdapter)getBaseListFragment().getListAdapter();
        adapter.changeAudioStates(event.getAudioID(), AudioListAdapter.STATE_WAITING, event.isWaiting());
    }



}
