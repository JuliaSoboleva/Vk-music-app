package com.soboleva.vkmusicloader.presenters;

import com.soboleva.vkmusicloader.vk.models.audios.Audio;
import com.soboleva.vkmusicloader.ui.adapters.AudioListAdapter;
import com.soboleva.vkmusicloader.ui.fragments.BaseListFragment;
import com.soboleva.vkmusicloader.utils.eventBusMessages.MessageAudioDownloadingEvent;
import com.soboleva.vkmusicloader.utils.eventBusMessages.MessageAudioWaitingEvent;
import com.soboleva.vkmusicloader.utils.eventBusMessages.MessageDownloadingProgressEvent;
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

    public void onEventMainThread(MessageAudioDownloadingEvent event){
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
