package com.soboleva.vkmusicapp.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.vk.sdk.VKUIHelper;
import timber.log.Timber;

public class VkMusicApplication extends Application {

    VkApi mVkApi = VkApi.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
        mVkApi.initialize();

        registerActivityLifecycleCallbacks(new LifecycleCallbacks());
    }

    private static class LifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Timber.d("onActivityCreated -> VKUIHelper.onCreate");
            VKUIHelper.onCreate(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Timber.d("onActivityResumed -> VKUIHelper.onResume");
            VKUIHelper.onResume(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            VKUIHelper.onDestroy(activity);
        }

    }
}
