package com.soboleva.vkmusicapp.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FriendListAdapter extends BaseAdapter {
    private List<Friend> mFriendList;
    private ImageLoader mImageLoader;
    DisplayImageOptions mOptions;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public FriendListAdapter(Context context) {
        mFriendList = new ArrayList<>();
        mImageLoader = ImageLoader.getInstance();

        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(false) // default
                .considerExifParams(false) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .build();
    }


    public void setFriendList(List<Friend> friendList) {
        mFriendList = friendList;
        notifyDataSetChanged();
    }

    public void setAddedFriendList(List<Friend> friendList) {
        mFriendList.addAll(friendList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFriendList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFriendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    // ViewHolder паттерн. Тут определяешь все контролы, что есть в layout'е твоего item'а
    private class ViewHolder {
        TextView mName;
        ImageView mPhoto;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        final Friend friend = (Friend) getItem(position);

        // ViewHolder паттерн. Вот эти две ветки прилично улучшают производительность при листании ListView
        // Без них оно будет тормозить.

        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_list, parent,
                    false);

            holder = new ViewHolder();

            holder.mName = (TextView) convertView.findViewById(R.id.text_friend_name);

            holder.mPhoto = (ImageView ) convertView.findViewById(R.id.image_friend_photo);
            //holder.mPhoto.setBorderColor(Color.LTGRAY);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // начиная отсюда заполняешь все элементы, что есть в твоем item, который входит в ListView
        // картинки, кнопки, всё подряд

        holder.mName.setText(friend.getFirstName() + " " + friend.getLastName());
        //todo holder.mPhoto.setImageResource();

        String imageUrl = friend.getPhoto100();

        mImageLoader.displayImage(imageUrl, holder.mPhoto, mOptions, animateFirstListener); // Запустили асинхронный показ картинки



        return convertView;
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



