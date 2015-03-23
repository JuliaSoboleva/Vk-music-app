package com.soboleva.vkmusicapp.utils;

import timber.log.Timber;

public class PathHelper {
    private static final String FILENAME_PATTERN = "%s - %s.mp3";

    public static String buildMp3FileName(String artist, String filename) {
        String formattedFilename = String.format(FILENAME_PATTERN, artist, filename);
        return removeSlashes(formattedFilename);
    }

    private static String removeSlashes(String s) {
        return s.replace("\\", "").replace("/", "");
    }

    public static String incrementFileName(String artist, String filename, int number) {
        String result = buildMp3FileName(artist, filename);
        result = result.replace(".mp3", "("+number+").mp3");
        Timber.d("counter filename = %s", result);
        return result;
    }
}
