package com.soboleva.vkmusicloader.vk.callbacks;

import com.soboleva.vkmusicloader.vk.models.photos.Photo;

public interface OnPhotoDownloadedListener {
    public void OnFriendPhotoDownloaded(Photo photo);

    public void onError();
}
