package com.soboleva.vkmusicapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.soboleva.vkmusicapp.AudioIntentService;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.presenters.AudioPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import timber.log.Timber;

import java.util.List;

public class AudioListActivity extends Activity {

    private Button mSearchButton;
    private EditText mSearchEditText;
    private ListView mAudioListView;
    //private ImageButton mSaveButton;
    private boolean mIsMyAudio;

    AudioPresenter mAudioPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.soboleva.vkmusicapp.R.layout.activity_audio_list);

        mIsMyAudio = true;

        setupPresenters();
        setupUI();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAudioPresenter.onActivityResult(this, requestCode, resultCode, data);
    }


    private void setupUI() {
        mSearchButton = (Button) findViewById(R.id.parse);
        mSearchEditText = (EditText) findViewById(R.id.search);
        mAudioListView = (ListView) findViewById(R.id.list);
        //mSaveButton = (ImageButton) findViewById(R.id.save);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchEditText.getText().toString().isEmpty()) {
                    mIsMyAudio = true;
                    mAudioPresenter.getMyAudio();
                } else {
                    mIsMyAudio = false;
                    mAudioPresenter.getSearchedAudio(mSearchEditText.getText().toString());
                }
                mAudioListView.smoothScrollToPosition(0);
            }
        });

        mAudioListView.setAdapter(new AudioListAdapter(new AudioListAdapter.OnButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                Timber.d("Downloading audio %s - %s", audio.getArtist(), audio.getTitle());
                Intent i = new Intent(getApplicationContext(), AudioIntentService.class);
                i.putExtra(AudioIntentService.URL, audio.getURL());
                i.putExtra(AudioIntentService.TITLE, audio.getTitle());
                i.putExtra(AudioIntentService.ARTIST, audio.getArtist());
                startService(i);

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
                            mAudioPresenter.getSearchedAudio(mSearchEditText.getText().toString(), available, AudioPresenter.PAGE_SIZE);
                        }
                    } else {
                        if (mIsMyAudio) {
                            mAudioPresenter.getMyAudio(available, total - available);
                        } else {
                            mAudioPresenter.getSearchedAudio(mSearchEditText.getText().toString(), available, total - available);
                        }
                    }
                    Timber.d("need to refresh audioList, totalItemCount = %d ", totalItemCount);
                }
            }
        });

    }

    private void setupPresenters() {
        mAudioPresenter = new AudioPresenter(this);
        mAudioPresenter.getMyAudio();

    }



    public void showAudio(List<Audio> audioList) {
        AudioListAdapter adapter = (AudioListAdapter) mAudioListView.getAdapter();
        adapter.setAudioList(audioList);

    }

    public void showWithAddedAudio(List<Audio> addedAudioList) {
        ((AudioListAdapter) mAudioListView.getAdapter()).setAddedAudioList(addedAudioList);

    }
}





