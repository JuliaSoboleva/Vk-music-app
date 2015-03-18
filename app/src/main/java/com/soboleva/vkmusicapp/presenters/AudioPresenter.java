package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.ui.fragments.BaseListFragment;

public abstract class AudioPresenter extends BaseListPresenter {


    protected AudioPresenter(BaseListFragment baseListFragment) {
        super(baseListFragment);
    }

    public void addAudio(Audio audio) {
        mVkApi.addAudio(audio.getID(), audio.getOwnerID());
    }



}
