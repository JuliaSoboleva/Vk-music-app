package com.soboleva.vkmusicapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.presenters.MainPresenter;
import timber.log.Timber;

public class MainActivity extends Activity {

    private Button mAuthorizeButton;
    private Button logoutButton;
    private Button mMyAudioButton;

    MainPresenter mMainPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainPresenter = new MainPresenter(this);

        setupUI();
        showButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mMainPresenter.onActivityResult(this, requestCode, resultCode, data);
    }

    private void setupUI() {
        mAuthorizeButton = (Button) findViewById(R.id.authorize);
        logoutButton = (Button) findViewById(R.id.logout);
        mAuthorizeButton.setOnClickListener(authorizeClick);
        logoutButton.setOnClickListener(logoutClick);
        mMyAudioButton = (Button) findViewById(R.id.audio);
        mMyAudioButton.setOnClickListener(audioClick);
    }

    private View.OnClickListener audioClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAudioActivity();
        }
    };

    private View.OnClickListener authorizeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Timber.d("Auth button click");
            mMainPresenter.authorize();
        }
    };

    private View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainPresenter.logout();
            showButtons();
        }
    };


    public void showErrorMessage() {
        // todo toast
    }

    void showButtons() {
        if (mMainPresenter.isLoggedIn()) {
            mAuthorizeButton.setVisibility(View.GONE);
            //mAuthorizeButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            mMyAudioButton.setVisibility(View.VISIBLE);
        } else {
            mAuthorizeButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
            mMyAudioButton.setVisibility(View.GONE);
        }
    }


    public void startAudioActivity() {
        Timber.d("MainActivity -> startAudioActivity()");
        startActivity(new Intent(MainActivity.this, AudioListActivity.class));
        finish();
    }
}
