package com.soboleva.vkmusicloader.presenters;

import com.soboleva.vkmusicloader.vk.VkApi;
import com.soboleva.vkmusicloader.ui.activities.AudioListActivity;

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
