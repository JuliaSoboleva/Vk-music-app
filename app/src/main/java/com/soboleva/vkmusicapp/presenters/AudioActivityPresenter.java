package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.soboleva.vkmusicapp.ui.activities.AudioListActivity;

public class AudioActivityPresenter {

    private final AudioListActivity mAudioActivity;
    private final VkApi mVkApi;

    public AudioActivityPresenter(AudioListActivity activity) {
        mAudioActivity = activity;
        mVkApi = VkApi.getInstance();
    }


    public void logOut() {
        mVkApi.logOut();
        //todo starting activity
    }



}
