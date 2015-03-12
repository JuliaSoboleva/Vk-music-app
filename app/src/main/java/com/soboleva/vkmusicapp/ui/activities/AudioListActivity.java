package com.soboleva.vkmusicapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.soboleva.vkmusicapp.R;
import com.soboleva.vkmusicapp.presenters.AudioActivityPresenter;
import com.soboleva.vkmusicapp.ui.fragments.SearchAudioListFragment;
import com.soboleva.vkmusicapp.ui.fragments.ViewPagerFragment;
import timber.log.Timber;

public class AudioListActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private SearchView mSearchView;

    private AudioActivityPresenter mAudioActivityPresenter;
    private FragmentManager mFragmentManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.soboleva.vkmusicapp.R.layout.activity_audio_list);
        mAudioActivityPresenter = new AudioActivityPresenter(this);

        setupUI();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Timber.d("WTF,onQueryTextSubmit,  %s", s);
                //todo замена фрагмента с viewPager-ом на фрагмент с результаами поиска
                //todo или обновление поиска!
                //todo не работает отмена
                Fragment searchAudioListFragment = SearchAudioListFragment.newInstance(AudioListActivity.this, s);
                mFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, searchAudioListFragment)
                .commit();

                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                Timber.d("WTF,onQueryTextChange, %s", mSearchView.getQuery());

                return false;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mFragmentManager.popBackStack();
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAudioActivityPresenter.onActivityResult(this, requestCode, resultCode, data);
    }


    private void setupUI() {

        mToolbar = (Toolbar)findViewById(R.id.toolbar_audio_activity);
        mToolbar.inflateMenu(R.menu.main_menu);

        setSupportActionBar(mToolbar);

        Menu mMenu = mToolbar.getMenu();
        ActionBar actionBar = getSupportActionBar();

        // получаем экземпляр FragmentTransaction
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // добавляем фрагмент
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        fragmentTransaction.add(R.id.fragment_container, viewPagerFragment);
        fragmentTransaction.commit();
    }






}





