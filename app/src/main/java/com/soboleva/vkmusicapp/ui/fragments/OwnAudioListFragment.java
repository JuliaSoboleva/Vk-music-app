package com.soboleva.vkmusicapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;
import com.soboleva.vkmusicapp.presenters.OwnAudioPresenter;
import com.soboleva.vkmusicapp.ui.adapters.AudioListAdapter;
import de.timroes.android.listview.EnhancedListView;
import timber.log.Timber;

public class OwnAudioListFragment extends AudioListFragment {

    SwipeRefreshLayout mSwipeRefreshLayout;

    // instanceOf constructor for creating fragment with arguments
    public static Fragment instantiate(Context context) {
        return Fragment.instantiate(context, OwnAudioListFragment.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_own_audio_list, container, false);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        ((EnhancedListView)getListView()).discardUndo();
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseListPresenter = new OwnAudioPresenter(this);
        mBaseListPresenter.getItems();

        setListAdapter(new AudioListAdapter(new AudioListAdapter.OnDownloadButtonClickListener() {
            @Override
            public void onClick(Audio audio) {
                Timber.d("on click OwnAudioListFragment");
                downloadAudio(audio);
            }
        }));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_layout);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Timber.d("Refresh");
//                // начинаем показывать прогресс
//                mSwipeRefreshLayout.setRefreshing(true);
//                // ждем 3 секунды и прячем прогресс
//                mSwipeRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //mSwipeRefreshLayout.setRefreshing(false);
//                        // говорим о том, что собираемся закончить
//                        //Toast.makeText(MainActivity.this, R.string.refresh_finished, Toast.LENGTH_SHORT).show();
//                        Timber.d("Refresh finished");
//                    }
//                }, 3000);
//            }
//        });

        //mSwipeRefreshLayout.setEnabled(false);

        ((EnhancedListView) getListView()).setDismissCallback(new EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView enhancedListView, final int position) {

                final Audio item = (Audio) getListAdapter().getItem(position);

                ((AudioListAdapter)getListAdapter()).remove(position);

                //((EnhancedListView) getListView()).delete(position);
                return new EnhancedListView.Undoable() {
                    // Reinsert the item to the adapter
                    @Override
                    public void undo() {
                        //((AudioPresenter) mBaseListPresenter).restoreAudio(item);
                        ((AudioListAdapter)getListAdapter()).insert(position, item);
                        Timber.d("Undo pressed for %s", item.getTitle());
                    }

                    // Return a string for your item
                    @Override
                    public String getTitle() {
                        return "Deleted '" + item.getTitle() + "'";
                    }

                    // Delete item completely from your persistent storage
                    @Override
                    public void discard() {
                        deleteAudio(item);
                        Timber.d("Deleting for %s", item.getTitle());
                    }
                };
            }
        });

        ((EnhancedListView) getListView()).enableSwipeToDismiss();
        ((EnhancedListView) getListView()).setSwipeDirection(EnhancedListView.SwipeDirection.END);
    }


}

