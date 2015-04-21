package com.soboleva.vkmusicapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.utils.eventBusMessages.MessageAudioWaitingEvent;
import de.greenrobot.event.EventBus;
import mbanje.kurt.fabbutton.FabButton;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;

public class AudioListAdapter extends BaseAdapter {
    public static final String STATE_DOWNLOADING = "downloading";
    public static final String STATE_WAITING = "waiting";

    private List<Audio> mAudioList;
    private OnDownloadButtonClickListener mDownloadButtonClickListener;
    private OnAddButtonClickListener mOnAddButtonClickListener;
    private boolean mAddAble;

    private ArrayList<String> mWaitingAudios = new ArrayList<String>();


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
        TextView mDuration;
        //Button mSaveButton;
        Button mAddButton;

        FabButton mFabButton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            //holder.mSaveButton = (Button) convertView.findViewById(R.id.save);
            holder.mDuration = (TextView) convertView.findViewById(R.id.text_duration);

            holder.mFabButton = (FabButton) convertView.findViewById(R.id.determinate);
            holder.mFabButton.hideProgressOnComplete(false);


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

        int duration = Integer.parseInt(audio.getDuration());
        int hours = duration / 3600;
        int minutes = duration / 60 - 60 * hours;
        int seconds = duration % 60;

        if (hours != 0) {
            //more than hour
            holder.mDuration.setText(String.format("%d:%02d:%02d", hours, minutes, seconds));

        } else {
            holder.mDuration.setText(String.format("%d:%02d", minutes, seconds));
        }

        holder.mFabButton.setTag(position);
        holder.mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("onClick for downloading button");

                EventBus.getDefault().post(new MessageAudioWaitingEvent(audio.getID(), true));
                //changeAudioStates(audio.getID(), STATE_WAITING, true);
                //audio.setWaiting(true);
                mDownloadButtonClickListener.onClick(audio);
            }
        });
        holder.mFabButton.setEnabled(!(audio.isDownloading() || audio.isWaiting()));

        holder.mFabButton.setProgress(audio.getDownloadingProgress());




        if (audio.isWaiting() != holder.mFabButton.isIndeterminate()) {
            holder.mFabButton.showProgress(true);
            holder.mFabButton.setIndeterminate(audio.isWaiting());
        }


        if (mAddAble) {
            holder.mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAddButtonClickListener.onClick(audio);
                }
            });

            holder.mAddButton.setEnabled(!audio.isAdded());
        }

        if (audio.getDownloadingProgress() == 0 && holder.mFabButton.isShowEndBitmap()) {
            //holder.mFabButton.resetIcon();
            holder.mFabButton.setShowEndBitmap(false);
        } else if (audio.getDownloadingProgress() == 100 && !holder.mFabButton.isShowEndBitmap()) {
            holder.mFabButton.setShowEndBitmap(true);
        }


        return convertView;
    }

    public void changeAudioStates(String audioID, String state, boolean value) {
        switch (state) {
            case STATE_DOWNLOADING:
                for (Audio audio : mAudioList) {
                    if (audio.getID().equals(audioID)) {
                        Timber.d("нужный id, change to %b", state);
                        audio.setDownloading(value);
                        //audio.setWaiting(false);

                    }
                }
                break;
            case STATE_WAITING:
                for (Audio audio : mAudioList) {
                    if (audio.getID().equals(audioID)) {
                        Timber.d("нужный id, change to %b", state);
                        audio.setWaiting(value);
                    }
                    if (value) {
                        mWaitingAudios.add(audioID);
                    } else {
                        mWaitingAudios.remove(audioID);
                    }
                }
                break;
        }

        this.notifyDataSetChanged();
    }

    public void changeProgress(String audioID, int progress) {
        for (Audio audio : mAudioList) {
            if (audio.getID().equals(audioID)) {
                audio.setDownloadingProgress(progress);
                Timber.d("Progress == %d", progress);
            }
        }
        this.notifyDataSetChanged();
    }


}
