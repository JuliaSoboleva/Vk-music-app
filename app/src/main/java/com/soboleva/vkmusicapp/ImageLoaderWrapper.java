package com.soboleva.vkmusicapp;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class ImageLoaderWrapper {

    private static ImageLoaderWrapper sInstance;


    public static synchronized ImageLoaderWrapper getInstance() {
        if (sInstance == null) {
            sInstance = new ImageLoaderWrapper();
        }

        return sInstance;
    }


    public void displayImage(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context).load(imageUrl).into(imageView);
    }

    public void displayImage(FragmentActivity activity, String imageUrl, ImageView imageView) {

//        GlideBuilder glideBuilder = new GlideBuilder(activity);
//        glideBuilder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        Glide.with(activity)
                .load(imageUrl)
                .into(imageView);

    }


}
