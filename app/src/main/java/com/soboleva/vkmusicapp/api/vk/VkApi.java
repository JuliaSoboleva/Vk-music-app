package com.soboleva.vkmusicapp.api.vk;

import android.content.Intent;
import com.google.gson.Gson;
import com.soboleva.vkmusicapp.api.vk.callbacks.AuthListener;
import com.soboleva.vkmusicapp.api.vk.callbacks.OnAudioDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.callbacks.OnAudioListDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.models.Audio;
import com.soboleva.vkmusicapp.api.vk.models.AudioResponseModel;
import com.soboleva.vkmusicapp.ui.activities.AudioListActivity;
import com.soboleva.vkmusicapp.ui.activities.MainActivity;
import com.vk.sdk.*;
import com.vk.sdk.api.*;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import timber.log.Timber;

public class VkApi {

    private static VkApi sInstance;

    private Gson mGson = new Gson();

    private AuthListener mAuthListener;
    private OnAudioListDownloadedListener mOnAudioListDownloadedListener;
    private OnAudioDownloadedListener mOnAudioDownloadedListener;

    private final String PATH = "/data/data/com.soboleva.vkmusicapp/";  //put the downloaded file here

    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.AUDIO
    };

    private VkApi() {
    }

    public static synchronized VkApi getInstance() {
        if (sInstance == null) {
            Timber.d("VkApi == null, create new VkApi");
            sInstance = new VkApi();
        }

        return sInstance;
    }

    public boolean wakeUpSession() {
        return VKSdk.wakeUpSession();
    }

    private VKSdkListener mVKSdkListener = new VKSdkListener() {
        @Override
        public void onRenewAccessToken(VKAccessToken token) {
            Timber.d("VkSdkListener onRenewAccessToken");
            super.onRenewAccessToken(token);
        }

        @Override
        public void onCaptchaError(VKError captchaError) {
            Timber.d("VkSdkListener onCaptchaError");
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            Timber.d("VkSdkListener onTokenExpired");
            VKSdk.authorize(sMyScope);
        }

        @Override
        public void onAccessDenied(VKError vkError) {
            Timber.d("VkSdkListener onAccessDenied");
            if (mAuthListener != null) {
                mAuthListener.onError();
            }
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            Timber.d("vksdkListener -> onReceiveNewToken");
            if (mAuthListener != null) {
                mAuthListener.onLogin();
            }
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            Timber.d("vksdkListener -> onAcceptUserToken");
            mAuthListener.onLogin();
        }
    };

    private VKRequest.VKRequestListener mAudioRequestListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            AudioResponseModel audioResponseModel = mGson.fromJson(response.responseString, AudioResponseModel.class);
            mOnAudioListDownloadedListener.onMusicListDownloaded(audioResponseModel.getAudioResponse().getAudioList());
        }

        @Override
        public void onError(VKError error) {
            //mResponseText.setText(error.toString());
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
            // you can show progress of the request if you want
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            //mResponseText.append(String.format("Attempt %d/%d failed\n", attemptNumber, totalAttempts));
        }
    };


    public void initialize() {
        Timber.d("VkApi.initialize()");
        VKSdk.initialize(mVKSdkListener, "3974615");
    }

    public void logOut() {
        VKSdk.logout();
    }

    public void authorize(AuthListener authListener) {
        Timber.d("VkApi.authorize(AuthListener)");
        mAuthListener = authListener;
        VKSdk.authorize(sMyScope, true, true);
    }

    public void getMyAudio(OnAudioListDownloadedListener onAudioListDownloadedListener) {
        mOnAudioListDownloadedListener = onAudioListDownloadedListener;
        VKRequest request = new VKRequest("audio.get", VKParameters.from(VKApiConst.COUNT, 20));
        request.executeWithListener(mAudioRequestListener);
    }

    public void getSearchedAudio(OnAudioListDownloadedListener onAudioListDownloadedListener, String searchRequest) {
        mOnAudioListDownloadedListener = onAudioListDownloadedListener;
        Timber.d("searching audio %s", searchRequest);
        VKRequest request = new VKRequest("audio.search", VKParameters.from(VKApiConst.Q, searchRequest, VKApiConst.SORT, 2));
        request.executeWithListener(mAudioRequestListener);
    }

    public void downloadAudio(OnAudioDownloadedListener onAudioDownloadedListener, Audio audio) {
        mOnAudioDownloadedListener = onAudioDownloadedListener;

    }

    public boolean isLoggedIn() {
        return VKSdk.isLoggedIn();
    }

    public void onActivityResult(MainActivity mainActivity, int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(mainActivity, requestCode, resultCode, data);
    }

    public void onActivityResult(AudioListActivity audioListActivity, int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(audioListActivity, requestCode, resultCode, data);
    }
}
