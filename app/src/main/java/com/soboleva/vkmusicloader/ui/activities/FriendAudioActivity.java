package com.soboleva.vkmusicloader.ui.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.soboleva.vkmusicloader.ImageLoaderWrapper;
import com.soboleva.vkmusicloader.R;
import com.soboleva.vkmusicloader.vk.models.friends.Friend;
import com.soboleva.vkmusicloader.presenters.FriendAudioActivityPresenter;
import com.soboleva.vkmusicloader.ui.fragments.FriendAudioListFragment;
import com.soboleva.vkmusicloader.ui.widgets.CustomImageView;
import de.greenrobot.event.EventBus;


public class FriendAudioActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    private FriendAudioActivityPresenter mFriendAudioActivityPresenter;

    private Friend mFriend;

    private CustomImageView mImageView;
    private Toolbar mToolbar;
    private View mListBackgroundView;
    private int mParallaxImageHeight;
    private TextView mToolbarTitle;

    private boolean mHasPhoto;

    private float mStartNameSize;
    private float mFinishNameSize;

    private FriendAudioListFragment mFragment;



    public static final String FRIEND = "friend";
    int mutedColor = R.attr.colorPrimary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_audio_list);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mFriend = (Friend)getIntent().getSerializableExtra(FRIEND);
        mHasPhoto = true;

        mFriendAudioActivityPresenter = new FriendAudioActivityPresenter(this, mFriend);

        setupUI();

    }

    private void setupUI() {
        mToolbar = (Toolbar)findViewById(R.id.toolbar_friend_audio_activity);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.colorPrimary)));

        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarTitle.setText((mFriend.getFirstName() + " " + mFriend.getLastName()));
        mToolbarTitle.setTypeface(MainActivity.mFont);


        mImageView = (CustomImageView)findViewById(R.id.image_profile_photo);
        mImageView.setOnBitmapSetListener(new CustomImageView.OnBitmapSetListener() {
            @Override
            public void onSet(Bitmap bitmap) {
                if (mHasPhoto) {

                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {

                            mutedColor = palette.getVibrantColor(R.color.colorPrimary); //getMutedColor(R.attr.colorPrimary);
                            int r = Color.red(mutedColor);
                            int g = Color.green(mutedColor);
                            int b = Color.blue(mutedColor);
                            double factor = 0.2126 * r + 0.7152 * g + 0.0722 * b;
                            if (factor < 50 || factor > 205) { //if vibrant color is too dark  or too light
                                mutedColor = palette.getMutedColor(R.color.colorPrimary);
                            }
                            mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mutedColor));
                        }
                    });

                    mFragment.showGradient();
                }
            }
        });

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);


        mListBackgroundView = findViewById(R.id.list_background);


        mFriendAudioActivityPresenter.getFriedPhoto();

        setupFriendAudioListFragment(mFriend);

    }

    private void setupFriendAudioListFragment(Friend friend) {
        final Fragment friendAudioListFragment = FriendAudioListFragment.instantiate(this, friend);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.friend_audio_container, friendAudioListFragment)
                .commit();
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof FriendAudioListFragment) {
            mFragment = (FriendAudioListFragment) fragment;
        }

    }

    public void setNameSize() {
        mStartNameSize = mFragment.getNameTitle().getTextSize() / getResources().getDisplayMetrics().scaledDensity;//sp
        mFinishNameSize = 18; //sp
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void showPhoto(String friendPhoto) {
        ImageLoaderWrapper.getInstance().displayImage(this, friendPhoto, mImageView);
    }

    public void hidePhoto() {
        mHasPhoto = false;
        mFragment.hidePhoto(mToolbar.getHeight());
        mImageView.setVisibility(View.INVISIBLE);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mToolbarTitle.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(((ObservableListView)mFragment.getListView()).getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (mHasPhoto) {
            float alpha = Math.min(1, (float) scrollY / (mParallaxImageHeight - mToolbar.getHeight()));

            mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, mutedColor));
            ViewHelper.setTranslationY(mImageView, -scrollY / 2);
            mFragment.getNameTitle().setTextSize(TypedValue.COMPLEX_UNIT_SP,
                    mStartNameSize - (mStartNameSize - mFinishNameSize) * alpha);

            // Translate list background
            ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mParallaxImageHeight));

            if ((float) scrollY / (mParallaxImageHeight - mToolbar.getHeight()) >= 1) {
                mToolbarTitle.setVisibility(View.VISIBLE);
            } else {
                mToolbarTitle.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }


}
