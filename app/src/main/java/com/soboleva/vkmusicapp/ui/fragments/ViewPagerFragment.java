package com.soboleva.vkmusicapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.astuetz.PagerSlidingTabStrip;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.ui.adapters.MyFragmentPagerAdapter;
import timber.log.Timber;

public class ViewPagerFragment extends Fragment {

    private ViewPager mPager;
    private PagerSlidingTabStrip mTabs;
    private MyFragmentPagerAdapter mPagerAdapter;

    private int mCurrentFragmentIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mCurrentFragmentIndex = 0;
        super.onCreate(savedInstanceState);
        Timber.d("viewPager -> onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("viewPager -> onCreateView");
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        setupPagerAdapter();

        // Bind the tabs to the ViewPager
        mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);

        setupOnPageChangeListener();

        return view;
    }



    private void setupPagerAdapter() {

        Timber.d("viewPager -> setupPagerAdapter");

        mPagerAdapter = new MyFragmentPagerAdapter(/*getActivity().getSupportFragmentManager()*/getChildFragmentManager(), getActivity());
        mPager.setAdapter(mPagerAdapter);

    }

    private void setupOnPageChangeListener() {
        mTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentFragmentIndex = position;
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


