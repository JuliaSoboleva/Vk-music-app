package com.soboleva.vkmusicapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.soboleva.vkmusicapp.AudioIntentService;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;
import com.soboleva.vkmusicapp.presenters.FriendAudioActivityPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import timber.log.Timber;

import java.util.List;

public class FriendAudioActivity extends ActionBarActivity {

    private ListView mAudioListView;
    private ImageView mImageView;
    private TextView mTextView;
    private Toolbar mToolbar;
    private FriendAudioActivityPresenter mPresenter;
    public static final String FRIEND = "friend";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_audio_list);

        mAudioListView = (ListView)findViewById(R.id.list_friend_audio);
        mImageView = (ImageView)findViewById(R.id.image_profile_photo);
        mTextView = (TextView)findViewById(R.id.text_profile_name);

        mAudioListView.setAdapter(new AudioListAdapter(new AudioListAdapter.OnDownloadButtonClickListener() {
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
        ImageLoader.getInstance().displayImage(friend.getPhoto100(), mImageView);
        mTextView.setText(friend.getFirstName() + " " + friend.getLastName());

        mPresenter = new FriendAudioActivityPresenter(this, friend);
        mPresenter.getAudio();

        mToolbar = (Toolbar)findViewById(R.id.toolbar_friend_audio_activity);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


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
