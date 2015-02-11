package com.soboleva.vkmusicapp.presenters;

import android.content.Intent;
import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.soboleva.vkmusicapp.api.vk.callbacks.AuthListener;
import com.soboleva.vkmusicapp.ui.activities.MainActivity;
import timber.log.Timber;

public class MainPresenter {
    private final VkApi mVkApi;
    private final MainActivity mMainActivity;

    public MainPresenter(MainActivity activity) {
        Timber.d("MainPresenter created");
        mMainActivity = activity;
        mVkApi = VkApi.getInstance();

        if (mVkApi.wakeUpSession()) {
            Timber.d("wakeUpSession == true");
            mMainActivity.startAudioActivity();
            mMainActivity.finish();
        }
    }

    public void onActivityResult(MainActivity mainActivity, int requestCode, int resultCode, Intent data) {
        mVkApi.onActivityResult(mainActivity, requestCode, resultCode, data);
    }

    public void authorize() {
        mVkApi.authorize(new AuthListener() {
            @Override
            public void onLogin() {
                Timber.d("mainPresenter -> authorize() -> onLogin()");
                mMainActivity.startAudioActivity();
            }

            @Override
            public void onError() {
                Timber.d("mainPresenter -> authorize() -> onError()");
                mMainActivity.showErrorMessage();
                // todo show error dialog
            }
        });
    }

    public void logout() {
        Timber.d("MainPresenter -> logout()");
        mVkApi.logOut();
    }

    public boolean isLoggedIn() {
        return mVkApi.isLoggedIn();
    }
}
