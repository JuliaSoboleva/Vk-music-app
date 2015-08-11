package com.soboleva.vkmusicapp.api.vk.callbacks;

import com.soboleva.vkmusicapp.api.vk.models.photos.Photo;

public interface OnPhotoDownloadedListener {
    public void OnFriendPhotoDownloaded(Photo photo);

    public void onError();
}
