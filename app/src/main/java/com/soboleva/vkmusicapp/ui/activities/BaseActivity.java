package com.soboleva.vkmusicapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.utils.NetworkHelper;
import com.soboleva.vkmusicapp.utils.eventBusMessages.MessageInternetStateChangedEvent;
import com.soboleva.vkmusicapp.utils.eventBusMessages.MessageNeedToUpdateEvent;
import de.greenrobot.event.EventBus;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showInternetState(NetworkHelper.isNetworkAvailable(this));

        EventBus.getDefault().register(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        showInternetState(NetworkHelper.isNetworkAvailable(this));
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInternetState(NetworkHelper.isNetworkAvailable(this));
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }


    public void onEventMainThread(MessageInternetStateChangedEvent event) {
        showInternetState(event.isState());
        if (event.isState()) {
            EventBus.getDefault().post(new MessageNeedToUpdateEvent());
        }
    }


    private void showInternetState(boolean state) {
        //проверяка
        if (state) {
            SnackbarManager.dismiss();
        } else {

            SnackbarManager.show(Snackbar.with(this) // context
                    .text(R.string.no_internet) // text to be displayed
                    .colorResource(R.color.colorRed) // change the background color
                    .swipeToDismiss(false)
                    .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE));
        }
    }
}


