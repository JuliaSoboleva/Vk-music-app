package com.soboleva.vkmusicapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.soboleva.vkmusicapp.AudioIntentService;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.Audio;
import com.soboleva.vkmusicapp.presenters.AudioPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import timber.log.Timber;

import java.util.List;

public class AudioListActivity extends Activity {

    private Button mSearchButton;
    private EditText mSearchEditText;
    private ListView mAudioListView;
    private ImageButton mSaveButton;
    private TextView mResponseText;

    AudioPresenter mAudioPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.soboleva.vkmusicapp.R.layout.activity_audio_list);

        mAudioPresenter = new AudioPresenter(this);
        setupUI();
        mAudioPresenter.getMyAudio();
        mAudioListView.setAdapter(new AudioListAdapter(new AudioListAdapter.OnButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                Timber.d("Downloading audio %s - %s", audio.getArtist(), audio.getTitle());
                //mAudioPresenter.downloadAudio(audio);
                Intent i = new Intent(getApplicationContext(), AudioIntentService.class);
                i.putExtra("url", audio.getURL());
                i.putExtra("title", audio.getTitle());
                i.putExtra("artist", audio.getArtist());
                startService(i);

            }
        }));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAudioPresenter.onActivityResult(this, requestCode, resultCode, data);
    }


    private void setupUI() {
        //Находим элементы
        mSearchButton = (Button) findViewById(R.id.parse);
        mSearchEditText = (EditText) findViewById(R.id.search);
        mAudioListView = (ListView) findViewById(R.id.list);
        mSaveButton = (ImageButton) findViewById(R.id.save);
        //Регистрируем onClick слушателей
        mSearchButton.setOnClickListener(mySearchListener);
        mResponseText = (TextView) findViewById(R.id.responseText);
    }

    //Слушатель OnClickListener для кнопки поиска
    private View.OnClickListener mySearchListener = new View.OnClickListener() {
        public void onClick(View v) {
            mAudioPresenter.getSearchedAudio(mSearchEditText.getText().toString());
        }
    };

    public void showAudio(List<Audio> audioList) {
        ((AudioListAdapter) mAudioListView.getAdapter()).setAudioList(audioList);

    }

    public void sayAudioDownloaded(String title) {
        Toast toast = Toast.makeText(getApplicationContext(), "Audio " + title + " downloaded", Toast.LENGTH_SHORT);
        toast.show();
    }


}





