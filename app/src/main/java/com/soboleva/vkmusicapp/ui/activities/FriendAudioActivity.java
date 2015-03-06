package com.soboleva.vkmusicapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import com.soboleva.vkmusicapp.AudioIntentService;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;
import com.soboleva.vkmusicapp.presenters.FriendAudioActivityPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import timber.log.Timber;

import java.util.List;

public class FriendAudioActivity extends Activity {

    private ListView mAudioListView;
    private ImageView mImageView;
    private FriendAudioActivityPresenter mPresenter;
    public static final String FRIEND = "friend";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_audio_list);

        mAudioListView = (ListView)findViewById(R.id.list_friend_audio);
        mImageView = (ImageView)findViewById(R.id.image_profile_photo);

        mAudioListView.setAdapter(new AudioListAdapter(new AudioListAdapter.OnButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                Timber.d("Downloading audio %s - %s", audio.getArtist(), audio.getTitle());
                Intent i = new Intent(FriendAudioActivity.this, AudioIntentService.class);
                i.putExtra(AudioIntentService.URL, audio.getURL());
                i.putExtra(AudioIntentService.TITLE, audio.getTitle());
                i.putExtra(AudioIntentService.ARTIST, audio.getArtist());
                startService(i);
            }
        }));

        Friend friend = (Friend)getIntent().getSerializableExtra(FRIEND);

        mPresenter = new FriendAudioActivityPresenter(this, friend);
        mPresenter.getAudio();


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
