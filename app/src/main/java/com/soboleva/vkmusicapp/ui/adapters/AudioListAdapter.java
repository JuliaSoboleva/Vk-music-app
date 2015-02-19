package com.soboleva.vkmusicapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;

import java.util.ArrayList;
import java.util.List;

public class AudioListAdapter extends BaseAdapter {
    private List<Audio> mAudioList;
    private OnButtonClickListener mButtonClickListener;


    public static interface OnButtonClickListener {
        void onClick(Audio audio);
    }

    public AudioListAdapter(OnButtonClickListener onButtonClickListener) {
        mAudioList = new ArrayList<>();
        mButtonClickListener = onButtonClickListener;
    }

    public void setAudioList(List<Audio> audioList) {
        mAudioList = audioList;
        notifyDataSetChanged();
    }

    public void setAddedAudioList(List<Audio> audioList) {
        mAudioList.addAll(audioList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAudioList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAudioList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    // ViewHolder паттерн. Тут определяешь все контролы, что есть в layout'е твоего item'а
    private class ViewHolder {
        TextView mTitle;
        TextView mArtist;
        Button mSaveButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        final Audio audio = (Audio) getItem(position);

        // ViewHolder паттерн. Вот эти две ветки прилично улучшают производительность при листании ListView
        // Без них оно будет тормозить.

        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_audio_list_item, parent,
                    false);

            holder = new ViewHolder();

            holder.mTitle = (TextView) convertView.findViewById(R.id.text1);
            holder.mArtist = (TextView) convertView.findViewById(R.id.text2);
            holder.mSaveButton = (Button) convertView.findViewById(R.id.save);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // начиная отсюда заполняешь все элементы, что есть в твоем item, который входит в ListView
        // картинки, кнопки, всё подряд

        holder.mTitle.setText(audio.getTitle());
        holder.mArtist.setText(audio.getArtist());
        holder.mSaveButton.setTag(position);
        holder.mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonClickListener.onClick(audio);
            }
        });


        return convertView;
    }


}
