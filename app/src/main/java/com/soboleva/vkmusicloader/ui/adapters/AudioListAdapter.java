package com.soboleva.vkmusicloader.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.soboleva.vkmusicloader.R;
import com.soboleva.vkmusicloader.vk.callbacks.OnAddButtonClickListener;
import com.soboleva.vkmusicloader.vk.callbacks.OnDownloadButtonClickListener;
import com.soboleva.vkmusicloader.vk.models.audios.Audio;
import com.soboleva.vkmusicloader.utils.eventBusMessages.MessageAudioWaitingEvent;
import com.soboleva.vkmusicloader.utils.eventBusMessages.MessageInterruptDownloadingEvent;
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


    public AudioListAdapter(OnDownloadButtonClickListener onDownloadButtonClickListener) {
        mAudioList = new ArrayList<>();
        mDownloadButtonClickListener = onDownloadButtonClickListener;
        mAddAble = false;
    }

    public AudioListAdapter(OnDownloadButtonClickListener onDownloadButtonClickListener,
                            OnAddButtonClickListener onAddButtonClickListener) {
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

                if (!audio.isDownloading() && !audio.isWaiting()) {
                    Timber.d("onClick for downloading button");
                    EventBus.getDefault().post(new MessageAudioWaitingEvent(audio.getID(), true));
                    mDownloadButtonClickListener.onClick(audio);
                } else if (audio.isDownloading()) {
                    Timber.d("onClick for interrupting downloading");
                    EventBus.getDefault().post(new MessageInterruptDownloadingEvent(audio.getID()));
                    audio.setDownloading(false);
                    //changeProgress(audio.getID(), 0);
                    audio.setDownloadingProgress(0);
                } else { //audio is waiting
                    Timber.d("onClick for interrupting waiting audio");
                    EventBus.getDefault().post(new MessageInterruptDownloadingEvent(audio.getID()));
                    audio.setWaiting(false);
                }
                notifyDataSetChanged();
            }
        });

        AudioAdapterHelper.checkFabButtonState(holder.mFabButton, audio);


        if (mAddAble) {
            holder.mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAddButtonClickListener.onClick(audio);
                }
            });

            holder.mAddButton.setEnabled(!audio.isAdded());
        }




        return convertView;
    }

    public void changeAudioStates(String audioID, String state, boolean value) {

        AudioAdapterHelper.changeAudioStates(audioID, state, value, mAudioList);
        this.notifyDataSetChanged();
    }

    public void changeProgress(String audioID, int progress) {

        AudioAdapterHelper.changeProgress(audioID, progress, mAudioList);
        this.notifyDataSetChanged();
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }



    public void remove(int position) {
        mAudioList.remove(position);
        notifyDataSetChanged();
    }


}
