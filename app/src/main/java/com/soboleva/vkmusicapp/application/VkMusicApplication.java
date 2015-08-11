package com.soboleva.vkmusicapp.application;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class VkMusicApplication extends Application {

    VkApi mVkApi = VkApi.getInstance();
    private boolean isVKAccessTokenChanged;

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                // VKAccessToken is invalid
                Timber.d("look ! onVKAccessTokenChanged");
                isVKAccessTokenChanged = true;
            } else {
                isVKAccessTokenChanged = false;
            }
        }
    };

    public static VkMusicApplication get(Context context) {
        return (VkMusicApplication) context.getApplicationContext();
    }

    public boolean isVKAccessTokenChanged() {
        return isVKAccessTokenChanged;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

//        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
//        }

        mVkApi.initialize(getApplicationContext());

        //ImageLoaderWrapper.getInstance().init(getApplicationContext());

        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);

        //registerActivityLifecycleCallbacks(new LifecycleCallbacks());
    }



}
