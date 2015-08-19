package com.soboleva.vkmusicloader.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.soboleva.vkmusicloader.utils.eventBusMessages.MessageInternetStateChangedEvent;
import de.greenrobot.event.EventBus;

public class WiFiConnectivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {

        MessageInternetStateChangedEvent event = new MessageInternetStateChangedEvent(NetworkHelper.isNetworkAvailable(context));
        EventBus.getDefault().post(event);
    }

}
