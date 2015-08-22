package com.soboleva.vkmusicloader.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontUtil {

    private static boolean sFontsLoaded = false;

    private static Typeface sFont;


    public static void apply(TextView textView) {
        Typeface typeface = getTypeface(textView.getContext());
        textView.setTypeface(typeface);
    }

    public static Typeface getTypeface(Context context) {
        if (!sFontsLoaded) {
            loadFont(context);
        }
        return sFont;
    }


    private static void loadFont(Context context) {
        sFont = Typeface.createFromAsset(context.getAssets(), "Lobster.ttf");
        sFontsLoaded = true;
    }

}
