package com.soboleva.vkmusicapp.api.vk;

import android.app.Activity;
import android.content.Intent;
import com.google.gson.Gson;
import com.soboleva.vkmusicapp.api.vk.callbacks.AuthListener;
import com.soboleva.vkmusicapp.api.vk.callbacks.OnAudioListDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.callbacks.OnFriendListDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.models.audios.AudioResponseModel;
import com.soboleva.vkmusicapp.api.vk.models.friends.FriendResponseModel;
import com.vk.sdk.*;
import com.vk.sdk.api.*;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import timber.log.Timber;

public class VkApi {

    //Этот ID предназначен только для примера. Пожалуйста замение его ID своего приложения.
    //private static String API_ID = "2904017";
    private static String API_ID = "3974615";

    private static VkApi sInstance;

    private Gson mGson = new Gson();

    private AuthListener mAuthListener;
    private OnAudioListDownloadedListener mOnAudioListDownloadedListener;
    private OnFriendListDownloadedListener mOnFriendListDownloadedListener;

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

    private VKSdkListener mAuthorizationListener = new VKSdkListener() {
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
            mOnAudioListDownloadedListener.onAudioListDownloaded(audioResponseModel.getAudioResponse().getAudioList(),
                    audioResponseModel.getAudioResponse().getTotalCount());
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

    private VKRequest.VKRequestListener mFriendRequestListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            FriendResponseModel friendResponseModel = mGson.fromJson(response.responseString, FriendResponseModel.class);
            mOnFriendListDownloadedListener.onFriendListDownloaded(friendResponseModel.getFriendResponse().getFriendList(),
                    friendResponseModel.getFriendResponse().getTotalCount());
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
        VKSdk.initialize(mAuthorizationListener, API_ID);
    }

    public void logOut() {
        VKSdk.logout();
    }

    public void authorize(AuthListener authListener) {
        Timber.d("VkApi.authorize(AuthListener)");
        mAuthListener = authListener;
        VKSdk.authorize(sMyScope, true, true);
    }

    public void getMyAudio(OnAudioListDownloadedListener onAudioListDownloadedListener, int offset, int count) {
        mOnAudioListDownloadedListener = onAudioListDownloadedListener;
        VKRequest request = new VKRequest("audio.get", VKParameters.from(VKApiConst.COUNT, count,
                VKApiConst.OFFSET, offset));
        request.executeWithListener(mAudioRequestListener);
    }

    public void getSearchedAudio(OnAudioListDownloadedListener onAudioListDownloadedListener, String searchRequest, int offset, int count) {
        mOnAudioListDownloadedListener = onAudioListDownloadedListener;
        Timber.d("searching audio %s", searchRequest);
        VKRequest request = new VKRequest("audio.search", VKParameters.from(VKApiConst.Q, searchRequest, VKApiConst.SORT, 2,
                VKApiConst.COUNT, count, VKApiConst.OFFSET, offset));
        request.executeWithListener(mAudioRequestListener);
    }

    public boolean isLoggedIn() {
        return VKSdk.isLoggedIn();
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(activity, requestCode, resultCode, data);
    }

    public void getFriends(OnFriendListDownloadedListener onFriendListDownloadedListener, int offset, int count) {
        mOnFriendListDownloadedListener = onFriendListDownloadedListener;
        VKRequest request = new VKRequest("friends.get", VKParameters.from(VKApiConst.COUNT, count,
                VKApiConst.OFFSET, offset, VKApiConst.FIELDS, "photo_100", "order", "hints"));
        request.executeWithListener(mFriendRequestListener);
    }

    public void addAudio(int audioID, int ownerID) {
        VKRequest request = new VKRequest("audio.add", VKParameters.from(VKApiConst.OWNER_ID, ownerID, "audio_id", audioID));
        //todo check it
        request.executeWithListener(null);
    }

    public void getAudio(OnAudioListDownloadedListener onAudioListDownloadedListener, int offset, int count, int friendID) {
        mOnAudioListDownloadedListener = onAudioListDownloadedListener;
        VKRequest request = new VKRequest("audio.get", VKParameters.from(VKApiConst.COUNT, count,
                VKApiConst.OFFSET, offset, VKApiConst.OWNER_ID, friendID));
        request.executeWithListener(mAudioRequestListener);

    }
}
