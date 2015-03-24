package com.soboleva.vkmusicapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.NotificationCompat;
import com.soboleva.vkmusicapp.utils.FileDownloader;
import com.soboleva.vkmusicapp.utils.PathHelper;
import timber.log.Timber;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE;
import static android.provider.MediaStore.Audio.AudioColumns.*;
import static android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;
import static android.provider.MediaStore.MediaColumns.TITLE;

public class AudioIntentService extends IntentService {
    public static final String PARAM_URL = "url";
    public static final String PARAM_ARTIST = "artist";
    public static final String PARAM_TITLE = "title";

    private static final int NOTIFICATION_ID = 1;

    private File mMusicDirectory;

    FileDownloader mFileDownloader = new FileDownloader(buildFileDownloaderCallback());

    private int mFullSize;
    private int mDownloadedSize;

    private String mCurrentArtist;
    private String mCurrentTitle;
    private String mAudioName;

    private File mFile;

    public AudioIntentService() {
        super("AudioIntentService");
    }

    public void onCreate() {
        super.onCreate();

        initMusicDirectory();
    }

    private FileDownloader.Callback buildFileDownloaderCallback() {
        return new FileDownloader.Callback() {
            @Override
            public void onPartDownloaded(int downloadedSize, File file) {
                mDownloadedSize = downloadedSize;

                if (mDownloadedSize == mFullSize) {
                    onAudioDownloaded(file);
                }
            }

            @Override
            public void onSizeReceived(int fullSize) {
                mFullSize = fullSize;

                Timber.d("on size received = %d", mFullSize);

                startProgressNotification();
            }

            @Override
            public void onError() {

            }
        };
    }

    private void onAudioDownloaded(File file) {
        ContentValues values = new ContentValues(7);

        values.put(TITLE, mCurrentTitle);
        values.put(ARTIST, mCurrentArtist);
        values.put(IS_MUSIC, true);
        values.put(DATE_ADDED, (int) (System.currentTimeMillis() / 1000));
        values.put(MIME_TYPE, "audio/3gpp");
        values.put(DATA, file.getAbsolutePath());
        values.put(DURATION, getDuration(file));

        ContentResolver contentResolver = getContentResolver();

        //получение преобразователя содержимого
        Uri newUri = contentResolver.insert(EXTERNAL_CONTENT_URI, values);

        // Notifiy the media application on the device
        sendBroadcast(new Intent(ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
    }

    private void initMusicDirectory() { // todo is external storage available?
        mMusicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        if (!mMusicDirectory.exists()) {

            boolean isCreated = mMusicDirectory.mkdirs();
            Timber.d("is music directory created = %b", isCreated);
        }
    }

    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        String remoteUrl = extras.getString(PARAM_URL);
        mCurrentArtist = extras.getString(PARAM_ARTIST);
        mCurrentTitle = extras.getString(PARAM_TITLE);

        String desiredFilename = PathHelper.buildMp3FileName(mCurrentArtist, mCurrentTitle);

        /*File*/
        mFile = new File(mMusicDirectory, desiredFilename);
        int counter = 0;
        while (mFile.exists()) {
            counter += 1;
            Timber.d("counter = %d", counter);
            mFile = new File(mMusicDirectory, PathHelper.incrementFileName(mCurrentArtist, mCurrentTitle, counter));
        }
        mAudioName = desiredFilename;

        mFileDownloader.download(remoteUrl, mFile);
    }

    private void startProgressNotification() {

        final NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle(mAudioName)
                .setContentText("Downloading...") // todo
                .setSmallIcon(R.drawable.ic_note)
                //.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_note))
                .setOngoing(true);

        HandlerThread handlerThread = new HandlerThread("NotificationThread");
        handlerThread.start();

        final Handler handler = new Handler(handlerThread.getLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mDownloadedSize != mFullSize) {

                    Timber.d("notify about full size %d and downloaded size %d", mFullSize, mDownloadedSize);

                    mBuilder.setProgress(mFullSize, mDownloadedSize, false);
                    notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

                    handler.postDelayed(this, 300);
                } else {

                    mBuilder.setContentText("Download complete") // todo
                            .setProgress(0, 0, false);

                    setNotificationOnClick(mBuilder);

                    notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

                    handler.removeCallbacks(this);
                }
            }
        });
    }

    private void setNotificationOnClick(NotificationCompat.Builder builder) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mFile), "audio/*");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOngoing(false);
    }

    private int getDuration(File file) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, Uri.parse(file.getAbsolutePath()));
        int duration = mediaPlayer.getDuration();

        mediaPlayer.release();

        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes);

        Timber.d("duration == %d min, %d sec", minutes, seconds);

        return duration;
    }
}
