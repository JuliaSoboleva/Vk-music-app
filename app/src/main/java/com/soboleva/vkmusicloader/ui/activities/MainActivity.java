package com.soboleva.vkmusicloader.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.soboleva.vkmusicloader.R;
import com.soboleva.vkmusicloader.presenters.MainPresenter;
import com.vk.sdk.util.VKUtil;
import timber.log.Timber;

import java.util.Arrays;

public class MainActivity extends BaseActivity {
    private MainPresenter mMainPresenter;

    public static Typeface mFont;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainPresenter = new MainPresenter(this);

        setupUI();

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Timber.d("fingerprints = %s", Arrays.toString(fingerprints));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mMainPresenter.onActivityResult(this, requestCode, resultCode, data);

    }

    public void defaultOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupUI() {

        mFont = Typeface.createFromAsset(getAssets(), "Lobster.ttf");

        TextView txt = (TextView) findViewById(R.id.app_name);
        txt.setTypeface(mFont);

        Button but = (Button) findViewById(R.id.authorize);
        but.setTypeface(mFont);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.authorize:
                        Timber.d("Auth button click");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mMainPresenter.authorize();
                            }
                        }, 500);
                        break;
                }
            }
        });


    }

    public void startAudioActivity() {
        Timber.d("MainActivity -> startAudioActivity()");
        startActivity(new Intent(MainActivity.this, AudioListActivity.class));
        finish();
    }


}
