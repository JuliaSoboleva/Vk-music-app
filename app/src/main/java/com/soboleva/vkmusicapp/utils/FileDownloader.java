package com.soboleva.vkmusicapp.utils;

import timber.log.Timber;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {
    private static final int BUFFER_SIZE = 1024;

    private int mDownloadedSize;

    private Callback mCallback;

    public static interface Callback {
        void onPartDownloaded(int downloadedSize, File file);

        void onSizeReceived(int fullSize);

        void onError();
    }

    public FileDownloader(Callback callback) {
        mCallback = callback;
    }

    public void download(String remoteUrl, File file) {

        resetFields();

        try {
            URL url = new URL(remoteUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();

            mCallback.onSizeReceived(connection.getContentLength());

            BufferedInputStream inputStream = new BufferedInputStream(is);
            FileOutputStream fileStream = new FileOutputStream(file);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileStream.write(buffer, 0, bytesRead);
                mDownloadedSize += bytesRead;

                mCallback.onPartDownloaded(mDownloadedSize, file);
            }

            fileStream.close();
            inputStream.close();
        } catch (IOException e) {
            Timber.e("something went wrong: %s", e.getMessage());

            e.printStackTrace();
        }
    }

    private void resetFields() {
        mDownloadedSize = 0;
    }
}
