package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import com.soboleva.vkmusicapp.AudioIntentService;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.presenters.AudioPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import timber.log.Timber;

import java.util.List;

public class AudioListFragment extends Fragment {

    private ListView mAudioListView;
    private AudioPresenter mAudioPresenter;
    private boolean mIsMyAudio;

    private String mLastQuery;

    // newInstance constructor for creating fragment with arguments
    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context, AudioListFragment.class.getName());
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsMyAudio = true;
        mAudioPresenter = new AudioPresenter(this);
        mAudioPresenter.getMyAudio();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_list, container, false);
        mAudioListView = (ListView) view.findViewById(R.id.audio_list);

        //todo
        mAudioListView.setAdapter(new AudioListAdapter(new AudioListAdapter.OnButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                Timber.d("Downloading audio %s - %s", audio.getArtist(), audio.getTitle());
                Intent i = new Intent(getActivity().getApplicationContext(), AudioIntentService.class);
                i.putExtra(AudioIntentService.URL, audio.getURL());
                i.putExtra(AudioIntentService.TITLE, audio.getTitle());
                i.putExtra(AudioIntentService.ARTIST, audio.getArtist());
                getActivity().getApplicationContext().startService(i);

            }
        }));

        mAudioListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //noop
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int total = mAudioPresenter.getTotalAudioCount();
                int available = mAudioPresenter.getAvailableAudioCount();
                if (firstVisibleItem + visibleItemCount >= totalItemCount - 1 && available < total && !mAudioPresenter.isDownloadingNow()) {
                    if (total - available >= AudioPresenter.PAGE_SIZE) {
                        if (mIsMyAudio) {
                            mAudioPresenter.getMyAudio(available, AudioPresenter.PAGE_SIZE);
                        } else {
                            mAudioPresenter.getSearchedAudio(mLastQuery, available, AudioPresenter.PAGE_SIZE);
                        }
                    } else {
                        if (mIsMyAudio) {
                            mAudioPresenter.getMyAudio(available, total - available);
                        } else {
                            mAudioPresenter.getSearchedAudio(mLastQuery, available, total - available);
                        }
                    }
                    Timber.d("need to refresh audioList, totalItemCount = %d ", totalItemCount);
                }
            }
        });

        return view;
    }

    public boolean isMyAudio() {
        return mIsMyAudio;
    }


    public void search(String query) {
        //todo
        mLastQuery = query;
        if (query.isEmpty()) {
            mIsMyAudio = true;
            mAudioPresenter.getMyAudio();
        } else {
            mIsMyAudio = false;
            mAudioPresenter.getSearchedAudio(query);
        }
        mAudioListView.smoothScrollToPosition(0);
    }


    public void showAudio(List<Audio> audioList) {
        AudioListAdapter adapter = (AudioListAdapter) mAudioListView.getAdapter();
        adapter.setAudioList(audioList);
        adapter.notifyDataSetChanged();
    }

    public void showWithAddedAudio(List<Audio> addedAudioList) {
        AudioListAdapter adapter = (AudioListAdapter) mAudioListView.getAdapter();
        adapter.setAddedAudioList(addedAudioList);
        adapter.notifyDataSetChanged();

    }


}

