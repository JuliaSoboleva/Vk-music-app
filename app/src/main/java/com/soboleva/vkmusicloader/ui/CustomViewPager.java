package com.soboleva.vkmusicloader.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.soboleva.swipemenu.MessageMenuStateChangedEvent;
import de.greenrobot.event.EventBus;

public class CustomViewPager extends ViewPager {

    private boolean mIsAnyMenuOpen;

    private int mDownX;


    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        EventBus.getDefault().register(this);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:

                if (getCurrentItem() == 0 && (mIsAnyMenuOpen || mDownX - event.getX() < 0)) {
                    //delegate swipe to child
                    return false;

                } else { //nothing special
                    break;
                }
        }
        return super.onInterceptTouchEvent(event);
    }



    public void onEventMainThread(MessageMenuStateChangedEvent event){
        mIsAnyMenuOpen = event.getState();
    }
}

