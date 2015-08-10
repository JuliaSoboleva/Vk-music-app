package com.soboleva.vkmusicapp.presenters;

import android.content.Intent;
import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.soboleva.vkmusicapp.application.VkMusicApplication;
import com.soboleva.vkmusicapp.ui.activities.MainActivity;
import com.soboleva.vkmusicapp.utils.NetworkHelper;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import timber.log.Timber;


public class MainPresenter {
    private final VkApi mVkApi;
    private final MainActivity mMainActivity;

    public MainPresenter(MainActivity activity) {
        Timber.d("MainPresenter created");
        mMainActivity = activity;
        mVkApi = VkApi.getInstance();

        if (mVkApi.isLoggedIn() && mVkApi.wakeUpSession() && !VkMusicApplication.get(mMainActivity).isVKAccessTokenChanged()) {
            Timber.d("wakeUpSession == true");
            mMainActivity.startAudioActivity();
            //mMainActivity.finish();
        } else {
            Timber.d("wakeUpSession = %b, isLoggedIn = %b", mVkApi.wakeUpSession(), mVkApi.isLoggedIn());
        }

    }

    public void onActivityResult(MainActivity mainActivity, int requestCode, int resultCode, Intent data) {
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // User passed Authorization
                mMainActivity.startAudioActivity();
            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback) ) {
            mMainActivity.defaultOnActivityResult(requestCode, resultCode, data);
        }
    }

    public void authorize() {
        //todo
        if (NetworkHelper.isNetworkAvailable(mMainActivity.getApplicationContext())) {
            mVkApi.login(mMainActivity);
//            mVkApi.authorize(new AuthListener() {
//                @Override
//                public void onLogin() {
//                    Timber.d("mainPresenter -> authorize() -> onLogin()");
//                    mMainActivity.startAudioActivity();
//                }
//
//                @Override
//                public void onError() {
//                    Timber.d("mainPresenter -> authorize() -> onError()");
//                    //mMainActivity.showErrorMessage();
//
//                }
//            });
        }
    }



}
