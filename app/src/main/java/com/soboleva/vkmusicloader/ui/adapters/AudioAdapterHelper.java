package com.soboleva.vkmusicloader.ui.adapters;

import com.soboleva.vkmusicloader.R;
import com.soboleva.vkmusicloader.vk.models.audios.Audio;
import mbanje.kurt.fabbutton.FabButton;
import timber.log.Timber;

import java.util.List;

public class AudioAdapterHelper {

    public static void checkFabButtonState(FabButton fabButton, Audio audio) {


        fabButton.setProgress(audio.getDownloadingProgress());


        if (audio.getDownloadingProgress() != 100) {
            if (audio.isWaiting() || audio.isDownloading()) {
                if (fabButton.getDrawablesRes()[0] != R.drawable.ic_cancel ||
                        fabButton.getDrawablesRes()[1] != R.drawable.ic_done) {
                    fabButton.setIcon(R.drawable.ic_cancel, R.drawable.ic_done);
                    Timber.d("FabButton sets cancelable");
                }

            } else { //normal state
                if (fabButton.getDrawablesRes()[0] != R.drawable.ic_downloading ||
                        fabButton.getDrawablesRes()[1] != R.drawable.ic_done) {
                    fabButton.setIcon(R.drawable.ic_downloading, R.drawable.ic_done);
                    Timber.d("FabButton sets normal");
                }
            }
        }

        if (audio.isWaiting() != fabButton.isIndeterminate()) {
            fabButton.showProgress(audio.isWaiting() || audio.isDownloading());
            fabButton.setIndeterminate(audio.isWaiting());
        }

        //if audio is downloaded -> show end bitmap
        //else no
        if((audio.getDownloadingProgress() == 100) != fabButton.isShowEndBitmap()) {
            fabButton.setShowEndBitmap(audio.getDownloadingProgress() == 100);
            if (fabButton.isShowEndBitmap()) {
                fabButton.setIcon(R.drawable.ic_done, R.drawable.ic_done);
            }
        }

    }

    public static void changeAudioStates(String audioID, String state, boolean value, List<Audio> audioList) {
        switch (state) {
            case AudioListAdapter.STATE_DOWNLOADING:
                for (Audio audio : audioList) {
                    if (audio.getID().equals(audioID)) {
                        audio.setDownloading(value);
                    }
                }
                break;
            case AudioListAdapter.STATE_WAITING:
                for (Audio audio : audioList) {
                    if (audio.getID().equals(audioID)) {
                        audio.setWaiting(value);
                    }
                }
                break;
        }
    }

    public static void changeProgress(String audioID, int progress, List<Audio> audioList) {
        for (Audio audio : audioList) {
            if (audio.getID().equals(audioID)) {
                audio.setDownloadingProgress(progress);
                Timber.d("Progress == %d", progress);
            }
        }
    }
}
