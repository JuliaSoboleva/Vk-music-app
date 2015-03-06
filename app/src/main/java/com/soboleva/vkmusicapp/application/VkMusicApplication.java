package com.soboleva.vkmusicapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
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

        //mImageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext())); // Проинициализировали конфигом по умолчанию
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);


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

        public static void initImageLoader(Context context) {
            // This configuration tuning is custom. You can tune every option, you may tune some of them,
            // or you can create default configuration by
            //  ImageLoaderConfiguration.createDefault(this);
            // method.

        }

    }
}
