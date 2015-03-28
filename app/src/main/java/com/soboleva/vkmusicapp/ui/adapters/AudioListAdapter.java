package com.soboleva.vkmusicapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;

public class AudioListAdapter extends BaseAdapter {
    private List<Audio> mAudioList;
    private OnDownloadButtonClickListener mDownloadButtonClickListener;
    private OnAddButtonClickListener mOnAddButtonClickListener;
    private boolean mAddAble;


    public static interface OnDownloadButtonClickListener {
        void onClick(Audio audio);
    }

    public static interface OnAddButtonClickListener {
        void onClick(Audio audio);
    }

    public AudioListAdapter(OnDownloadButtonClickListener onDownloadButtonClickListener) {
        mAudioList = new ArrayList<>();
        mDownloadButtonClickListener = onDownloadButtonClickListener;
        mAddAble = false;
    }

    public AudioListAdapter(OnDownloadButtonClickListener onDownloadButtonClickListener, OnAddButtonClickListener onAddButtonClickListener) {
        mAudioList = new ArrayList<>();
        mDownloadButtonClickListener = onDownloadButtonClickListener;
        mOnAddButtonClickListener = onAddButtonClickListener;
        mAddAble = true;
    }

    public void setAudioList(List<Audio> audioList) {
        mAudioList.clear();
        mAudioList.addAll(audioList);
    }

    public void setAddedAudioList(List<Audio> audioList) {
        mAudioList.addAll(audioList);

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
        Button mAddButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        final Audio audio = (Audio) getItem(position);

        // ViewHolder паттерн. Вот эти две ветки прилично улучшают производительность при листании ListView
        // Без них оно будет тормозить.

        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio_list, parent,
                    false);

            holder = new ViewHolder();

            holder.mTitle = (TextView) convertView.findViewById(R.id.text1);
            holder.mArtist = (TextView) convertView.findViewById(R.id.text2);
            holder.mSaveButton = (Button) convertView.findViewById(R.id.save);
            if (mAddAble) {
                holder.mAddButton = (Button) convertView.findViewById(R.id.button_add);
                holder.mAddButton.setVisibility(View.VISIBLE);
            }

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
                Timber.d("onClick for downloading button");
                mDownloadButtonClickListener.onClick(audio);
            }
        });
        holder.mSaveButton.setEnabled(!audio.isDownloading());

        if (mAddAble) {
            holder.mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAddButtonClickListener.onClick(audio);
                }
            });
        }



        return convertView;
    }

    public void changeAudioStates(String audioID, boolean state){
        for (Audio audio : mAudioList) {
            if (audio.getID().equals(audioID)) {
                Timber.d("нужный id, change to %b", state);
                audio.setDownloading(state);
            }
        }
        this.notifyDataSetChanged();
    }


}
