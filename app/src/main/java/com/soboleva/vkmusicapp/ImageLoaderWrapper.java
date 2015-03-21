package com.soboleva.vkmusicapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ImageLoaderWrapper {

    private static ImageLoaderWrapper sInstance;
    private DisplayImageOptions mOptions;

    private ImageLoaderWrapper() {
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher) // resource or drawable
                .build();
    }

    public static synchronized ImageLoaderWrapper getInstance() {
        if (sInstance == null) {
            sInstance = new ImageLoaderWrapper();
        }

        return sInstance;
    }

    public void init(Context context) {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
    }

    public void displayImage(String imageUrl, ImageView imageView){
        ImageLoader.getInstance().displayImage(imageUrl, imageView, mOptions, new AnimateFirstDisplayListener()); // Запустили асинхронный показ картинки
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 800);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
