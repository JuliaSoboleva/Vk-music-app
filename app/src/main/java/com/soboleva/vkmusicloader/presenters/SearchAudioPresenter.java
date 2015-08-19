package com.soboleva.vkmusicloader.presenters;

import com.soboleva.vkmusicloader.vk.callbacks.OnAudioListDownloadedListener;
import com.soboleva.vkmusicloader.vk.models.audios.Audio;
import com.soboleva.vkmusicloader.ui.fragments.AudioListFragment;
import com.soboleva.vkmusicloader.ui.fragments.BaseListFragment;
import com.soboleva.vkmusicloader.ui.fragments.SearchAudioListFragment;
import timber.log.Timber;

import java.util.List;

public class SearchAudioPresenter extends AudioPresenter {

    private String mSearchRequest;

    public SearchAudioPresenter(SearchAudioListFragment fragment, String searchRequest) {
        super(fragment);
        mSearchRequest = searchRequest;
    }

    @Override
    public void getItems(final int offset, int count) {
        mIsDownloadingNow = true;
        mVkApi.getSearchedAudio(new OnAudioListDownloadedListener() {
            @Override
            public void onAudioListDownloaded(List<Audio> audios, int totalCount) {
                if (totalCount == 0) {
                    ((AudioListFragment) mBaseListFragment).showMessage(BaseListFragment.STATE_NO_AUDIO);
                } else {
                    showItems(offset, audios, totalCount);
                }
            }

            @Override
            public void onError() {
                Timber.d("getSearchedAudio Error");
                mIsDownloadingNow = false;
                ((AudioListFragment) mBaseListFragment).showMessage(BaseListFragment.STATE_ERROR);
            }
        }, mSearchRequest, offset, count);
    }

//    public void setSearchRequest(String searchRequest) {
//        mSearchRequest = searchRequest;
//    }
}
