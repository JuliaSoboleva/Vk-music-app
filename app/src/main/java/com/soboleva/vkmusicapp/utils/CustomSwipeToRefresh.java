package com.soboleva.vkmusicapp.utils;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import timber.log.Timber;

public class CustomSwipeToRefresh extends SwipeRefreshLayout {

    private int mTouchSlop = 10;
    private float mPrevX;

    public CustomSwipeToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwipeToRefresh(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                Timber.d("xDiff = %f",  xDiff);


                if (xDiff > mTouchSlop) {
                    Timber.d("Swipe more then 25");
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);
    }

}
