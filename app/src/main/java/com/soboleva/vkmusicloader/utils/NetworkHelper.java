package com.soboleva.vkmusicloader.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkHelper {
    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
