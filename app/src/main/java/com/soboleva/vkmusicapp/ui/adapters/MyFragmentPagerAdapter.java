package com.soboleva.vkmusicapp.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.soboleva.vkmusicapp.ui.fragments.OwnAudioListFragment;
import com.soboleva.vkmusicapp.ui.fragments.FriendListFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    static final int PAGE_COUNT = 2;
    private Context mContext;
    private int mCurrentPosition;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        //return PageFragment.newInstance(position)
        mCurrentPosition = position;
        switch (position) {
            case 0:
                return OwnAudioListFragment.newInstance(mContext);
            case 1:
                return FriendListFragment.newInstance(mContext);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (position==0) ? "My audio list" : "Friend list";
    }


}


