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


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("viewPager -> onCreateView");
        View view = inflater.inflate(R.layout.view_empty_list, container, false);
        mImageView = (ImageView) view.findViewById(R.id.picture_empty_list);
        mTextView = (TextView)view.findViewById(R.id.text_empty_list);

        switch (mState) {
            case STATE_NO_AUDIO:
                mImageView.setImageResource(R.drawable.ic_owl);
                mTextView.setText(R.string.no_audio);
                break;
            case STATE_NO_INTERNET:
                mTextView.setText(R.string.no_internet);
                //todo
                break;
            case STATE_ERROR:
                if (isNetworkAvailable()) {
                    mTextView.setText(R.string.error);
                }
                else {
                    mTextView.setText(R.string.no_internet);
                }
                //todo
                break;
            default:
                break;

        }

        return view;
    }

    private  boolean isNetworkAvailable() {
        Context context = this.getActivity().getApplicationContext();
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }*/


}
