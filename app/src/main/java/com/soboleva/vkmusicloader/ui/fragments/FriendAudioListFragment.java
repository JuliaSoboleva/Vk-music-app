package com.soboleva.vkmusicloader.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.soboleva.vkmusicloader.R;
import com.soboleva.vkmusicloader.vk.callbacks.OnAddButtonClickListener;
import com.soboleva.vkmusicloader.vk.callbacks.OnDownloadButtonClickListener;
import com.soboleva.vkmusicloader.vk.models.audios.Audio;
import com.soboleva.vkmusicloader.vk.models.friends.Friend;
import com.soboleva.vkmusicloader.presenters.FriendAudioPresenter;
import com.soboleva.vkmusicloader.ui.activities.FriendAudioActivity;
import com.soboleva.vkmusicloader.ui.activities.MainActivity;
import com.soboleva.vkmusicloader.ui.adapters.AudioListAdapter;

public class FriendAudioListFragment extends AudioListFragment {

    private TextView mNameTitle;
    private Friend mFriend;

    private View mPaddingView;
    private View mGradient;

    ObservableScrollViewCallbacks mCallbacks;

    public static Fragment instantiate(Context context, Friend friend) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(FriendAudioActivity.FRIEND, friend);
        return Fragment.instantiate(context, FriendAudioListFragment.class.getName(), bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_audio_list, container, false);

        return view;
    }


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFriend = (Friend) getArguments().getSerializable(FriendAudioActivity.FRIEND);

        mBaseListPresenter = new FriendAudioPresenter(this, mFriend);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBaseListPresenter.getItems();

        int parallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        // Set padding view for ListView. This is the flexible space.
        mPaddingView = getActivity().getLayoutInflater().inflate(R.layout.padding_view, null);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                parallaxImageHeight);
        mPaddingView.setLayoutParams(lp);
        mPaddingView.setClickable(true);

        getListView().addHeaderView(mPaddingView);

        setListAdapter(new AudioListAdapter(new OnDownloadButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                downloadAudio(audio);
            }
        }, new OnAddButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                addAudio(audio);
                audio.setAdded(true);
            }
        }));

        mNameTitle = (TextView)mPaddingView.findViewById(R.id.friend_name_on_padding);
        mNameTitle.setText(mFriend.getFirstName() + " " + mFriend.getLastName());
        mNameTitle.setTypeface(MainActivity.mFont);

        mGradient = mPaddingView.findViewById(R.id.gradient);

        ((ObservableListView) getListView()).setScrollViewCallbacks(mCallbacks);

        ((FriendAudioActivity)mCallbacks).setNameSize();
    }

    public void showGradient(){
        mGradient.setVisibility(View.VISIBLE);
    }

    public TextView getNameTitle() {
        return mNameTitle;
    }

    public void hidePhoto(int toolbarHeight) {
        getListView().removeHeaderView(mPaddingView);

        mPaddingView = getActivity().getLayoutInflater().inflate(R.layout.padding_view, null);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                toolbarHeight);
        mPaddingView.setLayoutParams(lp);
        mPaddingView.setClickable(true);

        getListView().addHeaderView(mPaddingView);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (FriendAudioActivity)activity;
    }
}
