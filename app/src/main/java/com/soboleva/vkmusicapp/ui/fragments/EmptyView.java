package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EmptyView extends RelativeLayout {

    public static String STATE_KEY = "state";
    public static final int STATE_NO_AUDIO = 0;
    public static final int STATE_NO_INTERNET = 1;
    public static final int STATE_ERROR = 2;

    private ImageView mImageView;
    private TextView mTextView;

    public EmptyView(Context context) {
        super(context);

        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mTextView = new TextView(context);
        //mTextView.setId(R.id.text_empty_list);
        mImageView = new ImageView(context);
        //mImageView.setId(R.id.picture_empty_list);

        mTextView.setText("То что должно быть при пустом списке");

        addView(mTextView);
        addView(mImageView);
    }

}
