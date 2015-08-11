package com.soboleva.vkmusicapp.api.vk.callbacks;

import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;

import java.util.List;

public interface OnFriendListDownloadedListener {
    public void onFriendListDownloaded(List<Friend> friends, int totalCount);

    public void onError();
}
