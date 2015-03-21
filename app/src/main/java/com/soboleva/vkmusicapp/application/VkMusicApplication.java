package com.soboleva.vkmusicapp.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.soboleva.vkmusicapp.ImageLoaderWrapper;
import com.soboleva.vkmusicapp.api.vk.VkApi;
import timber.log.Timber;

public class VkMusicApplication extends Application {

    VkApi mVkApi = VkApi.getInstance();


    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
        mVkApi.initialize();

        ImageLoaderWrapper.getInstance().init(getApplicationContext());

        registerActivityLifecycleCallbacks(new LifecycleCallbacks());
    }

    private static class LifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Timber.d("onActivityCreated  %s", activity.getLocalClassName());
            VkApi.onCreate(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Timber.d("onActivityStarted %s", activity.getLocalClassName());

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Timber.d("onActivityResumed %s", activity.getLocalClassName());
            VkApi.onResume(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Timber.d("onActivityPaused %s", activity.getLocalClassName());

        }

        @Override
        public void onActivityStopped(Activity activity) {
            Timber.d("onActivityStopped %s", activity.getLocalClassName());

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Timber.d("onActivitySaveInstanceState %s", activity.getLocalClassName());

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Timber.d("onActivityDestroyed %s", activity.getLocalClassName());

            VkApi.onDestroy(activity);
        }

    }
}
