package com.soboleva.vkmusicapp.presenters;

import android.content.Intent;
import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.soboleva.vkmusicapp.ui.activities.AudioListActivity;

public class AudioActivityPresenter {

    private final AudioListActivity mAudioActivity;
    private final VkApi mVkApi;

    public AudioActivityPresenter(AudioListActivity activity) {
        mAudioActivity = activity;
        mVkApi = VkApi.getInstance();
    }

    public void onActivityResult(AudioListActivity audioListActivity, int requestCode, int resultCode, Intent data) {
        mVkApi.onActivityResult(audioListActivity, requestCode, resultCode, data);
    }

    public void logOut() {
        mVkApi.logOut();
        //todo starting activity
    }



}
