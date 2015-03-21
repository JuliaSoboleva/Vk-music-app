package com.soboleva.vkmusicapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import com.soboleva.vkmusicapp.ImageLoaderWrapper;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;
import com.soboleva.vkmusicapp.ui.fragments.FriendAudioListFragment;

public class FriendAudioActivity extends ActionBarActivity {

//    private ListView mAudioListView;
    private ImageView mImageView;
    private TextView mTextView;
    private Toolbar mToolbar;
    public static final String FRIEND = "friend";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_audio_list);
        setupUI();
    }

    private void setupUI() {
        mImageView = (ImageView)findViewById(R.id.image_profile_photo);
        mTextView = (TextView)findViewById(R.id.text_profile_name);

        Friend friend = (Friend)getIntent().getSerializableExtra(FRIEND);
        ImageLoaderWrapper.getInstance().displayImage(friend.getPhoto100(), mImageView);
        mTextView.setText(friend.getFirstName() + " " + friend.getLastName());

        mToolbar = (Toolbar)findViewById(R.id.toolbar_friend_audio_activity);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupFriendAudioListFragment(friend);

    }

    private void setupFriendAudioListFragment(Friend friend) {
        final Fragment friendAudioListFragment = FriendAudioListFragment.instantiate(this, friend);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.friend_audio_container, friendAudioListFragment)
                .commit();
    }



}
