package com.soboleva.vkmusicapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.apache.http.util.ByteArrayBuffer;
import timber.log.Timber;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AudioIntentService extends IntentService {

    private File cacheDir;
    private NotificationManager mNotifyManager;

    private final int id = 1;
    private int mFileSize = 0;
    private ByteArrayBuffer baf;

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
        String remoteUrl = intent.getExtras().getString("url");
        String location;
        String artist = intent.getExtras().getString("artist");
        String title = intent.getExtras().getString("title");
        String filename = title + ".mp3";

        File tmp = new File(cacheDir.getPath() + File.separator + filename);
        Timber.d("trying to write %s", cacheDir.getPath() + File.separator + filename);
        if (tmp.exists()) {
            location = tmp.getAbsolutePath();
            notifyProgress(true);
            stopSelf();
            return;
        }
        try {
            URL url = new URL(remoteUrl);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            if (httpCon.getResponseCode() != 200)
                throw new Exception("Failed to connect");
            InputStream is = httpCon.getInputStream();

            mFileSize = httpCon.getContentLength();
            Timber.d("file size in bytes %d", mFileSize);

            BufferedInputStream bis = new BufferedInputStream(is);
            baf = new ByteArrayBuffer(50);

            notifyProgress(false);

            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            FileOutputStream fos = new FileOutputStream(tmp);
            fos.write(baf.toByteArray());
            fos.close();
            is.close();


            // add new file to your media library
            ContentValues values = new ContentValues(4);
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
            Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Uri newUri = contentResolver.insert(base, values);

// Notifiy the media application on the device
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));

            location = tmp.getAbsolutePath();
            //notifyFinished(location, remoteUrl);
        } catch (Exception e) {
            Log.e("Service", "Failed!", e);
        }

    }

    private void notifyProgress(final boolean finished) {

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Audio Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_launcher);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (!finished) {
                            while (baf.length() < mFileSize) {
                                mBuilder.setProgress(mFileSize, baf.length(), false);
                                mNotifyManager.notify(id, mBuilder.build());
                                try {
                                    // Sleep for 1 seconds == 1000
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    Timber.d("sleep failure");
                                }
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
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
}
