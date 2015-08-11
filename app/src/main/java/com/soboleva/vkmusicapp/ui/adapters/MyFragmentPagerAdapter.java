package com.soboleva.vkmusicapp.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.ui.fragments.FriendListFragment;
import com.soboleva.vkmusicapp.ui.fragments.OwnAudioListFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    static final int PAGE_COUNT = 2;

    private Context mContext;

    private int mCurrentPosition;


    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        mCurrentPosition = 0;
    }

    @Override
    public Fragment getItem(int position) {
        mCurrentPosition = position;
        switch (position) {
            case 0:
                return OwnAudioListFragment.instantiate(mContext);
            case 1:
                return FriendListFragment.instantiate(mContext);
            default:
                return null;
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (position==0) ? mContext.getResources().getString(R.string.my_audio)
                : mContext.getResources().getString(R.string.friends);
    }


}


