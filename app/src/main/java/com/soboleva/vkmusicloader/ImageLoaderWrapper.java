package com.soboleva.vkmusicloader;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.soboleva.vkmusicloader.ui.activities.BaseActivity;

public class ImageLoaderWrapper {

    private static ImageLoaderWrapper sInstance;


    public static synchronized ImageLoaderWrapper getInstance() {
        if (sInstance == null) {
            sInstance = new ImageLoaderWrapper();
        }

        return sInstance;
    }


    public void displayImage(Context context, String imageUrl, ImageView imageView) {
        if (context != null && imageView != null) {
            Glide.with(context).load(imageUrl).into(imageView);
        }
    }

    public void displayImage(FragmentActivity activity, String imageUrl, ImageView imageView) {

        if (!((BaseActivity)activity).isActivityDestroyed()) {
            Glide.with(activity)
                    .load(imageUrl)
                    .into(imageView);
        }

    }


}
