package com.soboleva.vkmusicapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.soboleva.vkmusicapp.ImageLoaderWrapper;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;

import java.util.ArrayList;
import java.util.List;

public class FriendListAdapter extends BaseAdapter {
    private List<Friend> mFriendList;
    private Context mContext;

    public FriendListAdapter(Context context) {
        mFriendList = new ArrayList<>();
        mContext = context;
    }


    public void setFriendList(List<Friend> friendList) {
        mFriendList.clear();
        mFriendList.addAll(friendList);
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

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_list, parent, false);

            holder = new ViewHolder();
            holder.mName = (TextView) convertView.findViewById(R.id.text_friend_name);
            holder.mPhoto = (ImageView ) convertView.findViewById(R.id.image_friend_photo);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // начиная отсюда заполняешь все элементы, что есть в твоем item, который входит в ListView
        // картинки, кнопки, всё подряд
        holder.mName.setText(friend.getFirstName() + " " + friend.getLastName());
        String imageUrl = friend.getPhoto100();
        ImageLoaderWrapper.getInstance().displayImage(mContext, imageUrl, holder.mPhoto);
        return convertView;
    }




}



