package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.soboleva.vkmusicapp.AudioIntentService;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.BaseData;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.presenters.AudioPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import timber.log.Timber;

import java.util.List;

public class AudioListFragment extends BaseListFragment {


    private ImageView mEmptyImageView;
    private TextView mEmptyTextView;
    private View mEmptyView;


    public static Fragment instantiate(Context context) {
        return Fragment.instantiate(context, AudioListFragment.class.getName());
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("OnStart");
        getListView().setEmptyView(
                noItems(getResources().getString(R.string.no_audio)));

//        this.getListView().setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT));


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Timber.d("OnCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.d("onViewCreated");

        Timber.d("setEmptyView(mEmptyView)");
        /*Timber.d("isNetworkAvailable() = %b", isNetworkAvailable());
        if (!isNetworkAvailable()) {
            showEmpty(STATE_NO_INTERNET);
        }*/
    }

    public void downloadAudio(Audio audio) {
        Timber.d("Downloading audio %s - %s", audio.getArtist(), audio.getTitle());
        Intent i = new Intent(getActivity().getApplicationContext(), AudioIntentService.class);
        i.putExtra(AudioIntentService.PARAM_URL, audio.getURL());
        i.putExtra(AudioIntentService.PARAM_TITLE, audio.getTitle());
        i.putExtra(AudioIntentService.PARAM_ARTIST, audio.getArtist());
        i.putExtra(AudioIntentService.PARAM_AUDIO_ID, audio.getID());
        getActivity().getApplicationContext().startService(i);
    }

    public void addAudio(Audio audio) {
        AudioPresenter audioPresenter = (AudioPresenter) mBaseListPresenter;
        audioPresenter.addAudio(audio);
    }


    @Override
    public void showItems(List<? extends BaseData> dataList) {
        AudioListAdapter adapter = (AudioListAdapter) getListAdapter();
        //List<Audio> audios = dataList;
        adapter.setAudioList((List<Audio>) dataList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showWithAddedItems(List<? extends BaseData> dataList) {
        AudioListAdapter adapter = (AudioListAdapter) getListAdapter();
        adapter.setAddedAudioList((List<Audio>) dataList);
        adapter.notifyDataSetChanged();
    }


    /*public  boolean isNetworkAvailable() {
        Context context = this.getActivity().getApplicationContext();
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }*/

    private View noItems(String text) {

        mEmptyView = getActivity().getLayoutInflater().inflate(R.layout.view_empty_list, getListView(), false);

        mEmptyImageView = (ImageView) mEmptyView.findViewById(R.id.picture_empty_list);
        mEmptyTextView = (TextView) mEmptyView.findViewById(R.id.text_empty_list);

        mEmptyImageView.setVisibility(View.INVISIBLE);

        mEmptyTextView.setVisibility(View.INVISIBLE);

        ((ViewGroup) getListView().getParent()).addView(mEmptyView, 0);
        return mEmptyView;
    }

    public void showMessage(int stateID) {
        mEmptyImageView.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.VISIBLE);
        switch (stateID) {
            case STATE_NO_AUDIO:
                mEmptyImageView.setImageResource(R.drawable.ic_owl);
                mEmptyTextView.setText(R.string.no_audio);
                break;
            case STATE_NO_INTERNET:
                mEmptyTextView.setText(R.string.no_internet);
                //todo
                break;
            case STATE_ERROR:
                mEmptyTextView.setText(R.string.error);
                /*if (isNetworkAvailable()) {
                    text.setText(R.string.error);
                }
                else {
                    text.setText(R.string.no_internet);
                }*/
                //todo
                break;
            default:
                break;

        }

    }

}
