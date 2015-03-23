package com.soboleva.vkmusicapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import timber.log.Timber;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

public class AudioIntentService extends IntentService {

    public static final String URL = "url";
    public static final String ARTIST = "artist";
    public static final String TITLE = "title";
    private File cacheDir;
    private NotificationManager mNotifyManager;

    private final int id = 1;
    private int mFileSize = 0;
    private int mDownloadedSize;

    public AudioIntentService() {
        super("AudioIntentService");
    }

    public void onCreate() {
        super.onCreate();
        cacheDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        Timber.d("tmpLocation = %s", cacheDir.getAbsolutePath());
        if (!cacheDir.exists()) {
            Timber.d("was not needed dir");
            cacheDir.mkdirs();
            Timber.d("trying to mkdirs == %b", cacheDir.mkdirs());
        }

        //todo is external storage available?
    }

    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String remoteUrl = extras.getString(URL);
        String artist = extras.getString(ARTIST);
        String title = extras.getString(TITLE);

        String filename = getPureFileTitle(title);

        File tmp = new File(cacheDir.getPath(), filename);
        Timber.d("trying to write %s", cacheDir.getPath() + File.separator + filename);
        if (tmp.exists()) { // todo
            notifyProgress(true, title);
            stopSelf();
            return;
        }
        try {
            URL url = new URL(remoteUrl);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            if (httpCon.getResponseCode() != 200)
                throw new Exception("Failed to connect"); // todo
            InputStream is = httpCon.getInputStream();

            mFileSize = httpCon.getContentLength();
            Timber.d("file size in bytes %d", mFileSize);

            BufferedInputStream bis = new BufferedInputStream(is);
            FileOutputStream fos = new FileOutputStream(tmp);

            notifyProgress(false, title);

            byte[] buffer = new byte[1024]; // Adjust if you want
            int bytesRead;
            mDownloadedSize = 0;
            while ((bytesRead = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                mDownloadedSize += bytesRead;
                Timber.d("mDownloadedSize = %d, fileSize = %d", mDownloadedSize, mFileSize);
            }
            fos.close();
            is.close();

            // add new file to your media library
            ContentValues values = new ContentValues(7);
            long cur = System.currentTimeMillis();
            values.put(MediaStore.Audio.Media.TITLE, title);
            values.put(MediaStore.Audio.Media.ARTIST, artist);
            values.put(MediaStore.Audio.Media.IS_MUSIC, true);
            values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (cur / 1000));
            values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
            values.put(MediaStore.Audio.Media.DATA, tmp.getAbsolutePath());
            values.put(MediaStore.Audio.Media.DURATION, getDuration(tmp));
            ContentResolver contentResolver = getContentResolver();

            //получение преобразователя содержимого
            Uri newUri = contentResolver.insert(EXTERNAL_CONTENT_URI, values);

            // Notifiy the media application on the device
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));

        } catch (Exception e) {
            Log.e("Service", "Failed!", e);
        }

    }

    private void notifyProgress(final boolean finished, final String title) {

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(title)
                .setContentText("Downloading...")
                .setSmallIcon(R.drawable.ic_launcher);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (!finished) {
                            while (mDownloadedSize < mFileSize){
                                mBuilder.setProgress(mFileSize, mDownloadedSize, false);
                                mNotifyManager.notify(id, mBuilder.build());
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    Timber.d("sleep failure");
                                }
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete") // todo
                                // Removes the progress bar
                                .setProgress(0, 0, false);
                        mNotifyManager.notify(id, mBuilder.build());
                    }
                }
// Starts the thread by calling the run() method in its Runnable
        ).start();
    }

    private int getDuration(File file) {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), Uri.parse(file.getAbsolutePath()));
        int duration = mp.getDuration();

        Timber.d("duration == %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        return duration;
    }

    private String getPureFileTitle(String oldTitle) {
        return oldTitle.replace("\\", "").replace("/", "")+".mp3";
    }
}
