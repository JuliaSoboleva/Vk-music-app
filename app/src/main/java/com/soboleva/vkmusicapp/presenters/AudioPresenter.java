package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.ui.fragments.BaseListFragment;
import timber.log.Timber;

public abstract class AudioPresenter extends BaseListPresenter {


    protected AudioPresenter(BaseListFragment baseListFragment) {
        super(baseListFragment);
        //EventBus.getDefault().register(this);
    }

    public void addAudio(Audio audio) {
        Timber.d("Adding audio %s", audio.getTitle());
        mVkApi.addAudio(audio.getID(), audio.getOwnerID());
    }

//    public void onEvent(MessageEvent event){
//        Timber.d("onEvent works");
//        AudioListAdapter adapter = (AudioListAdapter)getBaseListFragment().getListAdapter();
//        adapter.changeAudioStates(event.getAudioID(), event.isDownloading());
//    }



}
