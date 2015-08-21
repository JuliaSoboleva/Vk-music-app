package com.soboleva.vkmusicloader;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.NotificationCompat;
import com.soboleva.vkmusicloader.utils.FileDownloader;
import com.soboleva.vkmusicloader.utils.PathHelper;
import com.soboleva.vkmusicloader.utils.eventBusMessages.MessageAudioDownloadingEvent;
import com.soboleva.vkmusicloader.utils.eventBusMessages.MessageDownloadingProgressEvent;
import com.soboleva.vkmusicloader.utils.eventBusMessages.MessageInterruptDownloadingEvent;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

import java.io.File;
import java.util.ArrayList;

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
    public static final String PARAM_AUDIO_ID = "audioID";
    public static final String PARAM_DURATION = "duration";

    private static final int NOTIFICATION_ID = 1;

    private File mMusicDirectory;

    FileDownloader mFileDownloader = new FileDownloader(buildFileDownloaderCallback());

    private int mFullSize;
    private int mDownloadedSize;

    private String mCurrentArtist;
    private String mCurrentTitle;
    private String mAudioName;
    private String mAudioID;
    private int mDuration; // in ms

    private File mFile;

    private ArrayList<String> mInterruptedList = new ArrayList<String>();

    public AudioIntentService() {
        super("AudioIntentService");
        EventBus.getDefault().register(this);
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
                Timber.d("mCallback onError сейчас должен появиться toast");
                //todo
            }
        };
    }

    private void onAudioDownloaded(File file) {
        EventBus.getDefault().post(new MessageDownloadingProgressEvent(mAudioID, 100));

        ContentValues values = new ContentValues(7);

        values.put(TITLE, mCurrentTitle);
        values.put(ARTIST, mCurrentArtist);
        values.put(IS_MUSIC, true);
        values.put(DATE_ADDED, (int) (System.currentTimeMillis() / 1000));
        values.put(MIME_TYPE, "audio/3gpp");
        values.put(DATA, file.getAbsolutePath());
        values.put(DURATION, /*getDuration(file)*/ mDuration);

        ContentResolver contentResolver = getContentResolver();

        //получение преобразователя содержимого
        Uri newUri = contentResolver.insert(EXTERNAL_CONTENT_URI, values);

        // Notifiy the media application on the device
        sendBroadcast(new Intent(ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));

        EventBus.getDefault().post(new MessageAudioDownloadingEvent(mAudioID, false));

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
        mAudioID = extras.getString(PARAM_AUDIO_ID);
        mDuration = 1000*extras.getInt(PARAM_DURATION);

        if (mInterruptedList.contains(mAudioID)){
            mInterruptedList.remove(mAudioID);
            Timber.d("mInterruptedList remove element");
            Timber.d("mInterruptedList contains %d", mInterruptedList.size());
            return;
        }

        EventBus.getDefault().post(new MessageAudioDownloadingEvent(mAudioID, true));
        EventBus.getDefault().post(new MessageDownloadingProgressEvent(mAudioID, 0));

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
                .setContentText(getResources().getString(R.string.downloading))
                .setSmallIcon(R.drawable.ic_note_white)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_notification_owl))
                .setOngoing(true);

        HandlerThread handlerThread = new HandlerThread("NotificationThread");
        handlerThread.start();

        final Handler handler = new Handler(handlerThread.getLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mDownloadedSize != mFullSize && !mFileDownloader.isInterrupted()) {

                    mBuilder.setProgress(mFullSize, mDownloadedSize, false);
                    EventBus.getDefault().post(new MessageDownloadingProgressEvent(mAudioID, (int) ((float) mDownloadedSize / (float) mFullSize * 100)));

                    notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

                    handler.postDelayed(this, 300);
                } else {

                    if (mFileDownloader.isInterrupted()) {
                        mBuilder.setContentText(getResources().getString(R.string.canceled))
                                .setProgress(0, 0, false);
                    } else {
                        mBuilder.setContentText(getResources().getString(R.string.complete))
                                .setProgress(0, 0, false);
                    }

                    setNotificationOnClick(mBuilder);
                    notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

                    handler.removeCallbacks(this);
                }
            }
        });
    }

    private void setNotificationOnClick(NotificationCompat.Builder builder) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW); //ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.fromFile(mFile), "audio/mp3");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOngoing(false);

    }

    //todo
//    private int getDuration(File file) {
//        MediaPlayer mediaPlayer = MediaPlayer.create(this, Uri.parse(file.getAbsolutePath()));
//        int duration = mediaPlayer.getDuration();
//
//        mediaPlayer.release();
//
//        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes);
//
//        Timber.d("duration == %d min, %d sec", minutes, seconds);
//
//        mediaPlayer.release();
//        return duration;
//    }

    public void onEventBackgroundThread(MessageInterruptDownloadingEvent event) {
        Timber.d("Для того аудиио отмена? %b", mAudioID.equals(event.getAudioID()));
        if(mAudioID.equals(event.getAudioID())) {
            mFileDownloader.setIsInterrupted(true);
            if (mInterruptedList.contains(event.getAudioID())){
                mInterruptedList.remove(event.getAudioID());
                Timber.d("mInterruptedList remove element");
                Timber.d("mInterruptedList contains %d", mInterruptedList.size());
            }
        } else {
            //user interrupt future downloading for audio which is waiting now
            if (!mInterruptedList.contains(event.getAudioID())) {
                mInterruptedList.add(event.getAudioID());
            }
            Timber.d("mInterruptedList add element");
            Timber.d("mInterruptedList contains %d", mInterruptedList.size());
        }
        //mIsInterrupted = true;
    }

}
