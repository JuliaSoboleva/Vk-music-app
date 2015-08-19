package com.soboleva.vkmusicloader.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.astuetz.PagerSlidingTabStrip;
import com.soboleva.vkmusicloader.R;
import com.soboleva.vkmusicloader.ui.CustomViewPager;
import com.soboleva.vkmusicloader.ui.adapters.MyFragmentPagerAdapter;
import timber.log.Timber;

public class ViewPagerFragment extends Fragment {

    private CustomViewPager mPager;
    private PagerSlidingTabStrip mTabs;
    private MyFragmentPagerAdapter mPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        mPager = (CustomViewPager) view.findViewById(R.id.pager);
        setupPagerAdapter();

        // Bind the tabs to the ViewPager
        mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);

        setupOnPageChangeListener();

        return view;
    }



    private void setupPagerAdapter() {

        mPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), getActivity());
        mPager.setAdapter(mPagerAdapter);

    }

    private void setupOnPageChangeListener() {
        mTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Timber.d("onPageSelected, position = %d", position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

}


