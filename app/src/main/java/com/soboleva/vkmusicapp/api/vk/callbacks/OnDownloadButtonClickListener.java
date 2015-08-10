package com.soboleva.vkmusicapp.api.vk.callbacks;

import com.soboleva.vkmusicapp.api.vk.models.audios.Audio;

public interface OnDownloadButtonClickListener {
    void onClick(Audio audio);
}
