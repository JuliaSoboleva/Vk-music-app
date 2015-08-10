package com.soboleva.vkmusicapp.ui.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.soboleva.vkmusicapp.utils.BitmapUtils;

public class CustomImageView extends ImageView {
    public interface OnBitmapSetListener {
        void onSet(Bitmap bitmap);
    }

    private OnBitmapSetListener mOnBitmapSetListener;

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnBitmapSetListener(OnBitmapSetListener onBitmapSetListener) {
        mOnBitmapSetListener = onBitmapSetListener;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);

        if (drawable != null) {
            onBitmapSet(BitmapUtils.drawableToBitmap(drawable));
        }
    }

    private void onBitmapSet(Bitmap bm) {
        if (mOnBitmapSetListener != null) {
            mOnBitmapSetListener.onSet(bm);
        }
    }
}
