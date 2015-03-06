package com.soboleva.vkmusicapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.presenters.AudioActivityPresenter;
import com.soboleva.vkmusicapp.ui.adapters.MyFragmentPagerAdapter;
import com.soboleva.vkmusicapp.ui.fragments.AudioListFragment;
import timber.log.Timber;

public class AudioListActivity extends FragmentActivity {

    private Button mSearchButton;
    private EditText mSearchEditText;

    private ViewPager pager;
    private MyFragmentPagerAdapter pagerAdapter;

    private AudioActivityPresenter mAudioActivityPresenter;

    private int mCurrentFragmentIndex;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.soboleva.vkmusicapp.R.layout.activity_audio_list);

        mCurrentFragmentIndex = 0;

        mAudioActivityPresenter = new AudioActivityPresenter(this);

        setupUI();
        setupPager();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAudioActivityPresenter.onActivityResult(this, requestCode, resultCode, data);
    }


    private void setupUI() {
        mSearchButton = (Button) findViewById(R.id.parse);
        mSearchEditText = (EditText) findViewById(R.id.search);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("search click, mCurrentFragmentIndex = %d", mCurrentFragmentIndex);
                String query = mSearchEditText.getText().toString();
                Timber.d("search = %s", query);
                switch (mCurrentFragmentIndex) {
                    case 0:
                        AudioListFragment fragment = (AudioListFragment)getSupportFragmentManager()
                                .findFragmentByTag(getFragmentTag(pager.getId(), mCurrentFragmentIndex));
                        fragment.search(query);
                        return;
                    case 1:
                        //todo
                        return;
                    default:
                        return;
                }
            }
        });

    }

    private void setupPager() {

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //Log.d(TAG, "onPageSelected, position = " + position);
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


    private String getFragmentTag(int viewPagerId, int fragmentPosition)
    {
        return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
    }


}





