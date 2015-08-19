package com.soboleva.vkmusicloader.vk;

import android.app.Activity;
import android.content.Context;
import com.google.gson.Gson;
import com.soboleva.vkmusicloader.vk.callbacks.AuthListener;
import com.soboleva.vkmusicloader.vk.callbacks.OnAudioListDownloadedListener;
import com.soboleva.vkmusicloader.vk.callbacks.OnFriendListDownloadedListener;
import com.soboleva.vkmusicloader.vk.callbacks.OnPhotoDownloadedListener;
import com.soboleva.vkmusicloader.vk.models.audios.AudioResponseModel;
import com.soboleva.vkmusicloader.vk.models.friends.FriendResponseModel;
import com.soboleva.vkmusicloader.vk.models.photos.PhotoResponseModel;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.*;
import timber.log.Timber;

public class VkApi {

    //private static String API_ID = "4869131";

    private static VkApi sInstance;

    private Gson mGson = new Gson();

    private AuthListener mAuthListener;

    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
//            VKScope.WALL,
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

    public void login(Activity activity) {
        VKSdk.login(activity, sMyScope);
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public boolean wakeUpSession() {
        return VKSdk.wakeUpSession(null);
    }

    private VKRequest.VKRequestListener mAuthorizationListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            super.attemptFailed(request, attemptNumber, totalAttempts);
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
            super.onProgress(progressType, bytesLoaded, bytesTotal);
        }
    };


    public void initialize(Context appContext) {
        Timber.d("VkApi.initialize()");
//        VKSdk.initialize(mAuthorizationListener, API_ID);
        VKSdk.initialize(appContext );
    }

    public void logOut() {
        VKSdk.logout();
    }


    public void getMyAudio(final OnAudioListDownloadedListener onAudioListDownloadedListener, int offset, int count) {
        VKRequest request = new VKRequest("audio.get", VKParameters.from(VKApiConst.COUNT, count,
                VKApiConst.OFFSET, offset));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                AudioResponseModel audioResponseModel = mGson.fromJson(response.responseString, AudioResponseModel.class);
                onAudioListDownloadedListener.onAudioListDownloaded(audioResponseModel.getAudioResponse().getAudioList(),
                        audioResponseModel.getAudioResponse().getTotalCount());
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                onAudioListDownloadedListener.onError();
            }
        });
    }

    public void getSearchedAudio(final OnAudioListDownloadedListener onAudioListDownloadedListener, String searchRequest, int offset, int count) {
        Timber.d("searching audio %s", searchRequest);
        VKRequest request = new VKRequest("audio.search", VKParameters.from(VKApiConst.Q, searchRequest, VKApiConst.SORT, 2,
                VKApiConst.COUNT, count, VKApiConst.OFFSET, offset));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                AudioResponseModel audioResponseModel = mGson.fromJson(response.responseString, AudioResponseModel.class);
                onAudioListDownloadedListener.onAudioListDownloaded(audioResponseModel.getAudioResponse().getAudioList(),
                        audioResponseModel.getAudioResponse().getTotalCount());
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                onAudioListDownloadedListener.onError();
            }
        });
    }

    public boolean isLoggedIn() {
        return VKSdk.isLoggedIn();
    }


    public void getFriends(final OnFriendListDownloadedListener onFriendListDownloadedListener, int offset, int count) {
        VKRequest request = new VKRequest("friends.get", VKParameters.from(VKApiConst.COUNT, count,
                VKApiConst.OFFSET, offset, VKApiConst.FIELDS, "photo_100, photo_200_orig", "order", "hints"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                FriendResponseModel friendResponseModel = mGson.fromJson(response.responseString, FriendResponseModel.class);
                onFriendListDownloadedListener.onFriendListDownloaded(friendResponseModel.getFriendResponse().getFriendList(),
                        friendResponseModel.getFriendResponse().getTotalCount());
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                onFriendListDownloadedListener.onError();
            }
        });
    }

    public void addAudio(String audioID, String ownerID) {
        VKRequest request = new VKRequest("audio.add", VKParameters.from(VKApiConst.OWNER_ID, ownerID, "audio_id", audioID));
        //todo check it
        request.executeWithListener(null);
    }

    public void deleteAudio(String audioID, String ownerID) {
        VKRequest request = new VKRequest("audio.delete", VKParameters.from(VKApiConst.OWNER_ID, ownerID, "audio_id", audioID));
        request.executeWithListener(null);
    }

    public void restoreAudio(String audioID, String ownerID) {
        VKRequest request = new VKRequest("audio.restore", VKParameters.from(VKApiConst.OWNER_ID, ownerID, "audio_id", audioID));
        request.executeWithListener(null);
    }

    public void getAudio(final OnAudioListDownloadedListener onAudioListDownloadedListener, int offset, int count, String friendID) {
        VKRequest request = new VKRequest("audio.get", VKParameters.from(VKApiConst.COUNT, count,
                VKApiConst.OFFSET, offset, VKApiConst.OWNER_ID, friendID));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                AudioResponseModel audioResponseModel = mGson.fromJson(response.responseString, AudioResponseModel.class);
                onAudioListDownloadedListener.onAudioListDownloaded(audioResponseModel.getAudioResponse().getAudioList(),
                        audioResponseModel.getAudioResponse().getTotalCount());
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                onAudioListDownloadedListener.onError();
            }
        });

    }

    public void getFriendPhoto(final OnPhotoDownloadedListener onPhotoDownloadedListener, String friendID) {
        VKRequest request = new VKRequest("photos.get", VKParameters.from(VKApiConst.OWNER_ID, friendID,
                VKApiConst.ALBUM_ID, "profile", VKApiConst.REV, "1", VKApiConst.COUNT, "1"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                PhotoResponseModel photoResponseModel = mGson.fromJson(response.responseString, PhotoResponseModel.class);
                if (photoResponseModel.getPhotoResponse().getCount() != 0) {
                    onPhotoDownloadedListener.OnFriendPhotoDownloaded(photoResponseModel.getPhotoResponse().getPhoto());
                } else {
                    onPhotoDownloadedListener.onError();
                }

            }
        });
    }

//    public static void onCreate(Activity activity) {
//        VKUIHelper.onCreate(activity);
//    }
//
//    public static void onResume(Activity activity) {
//        VKUIHelper.onResume(activity);
//    }
//
//    public static void onDestroy(Activity activity) {
//        VKUIHelper.onDestroy(activity);
//    }
}
