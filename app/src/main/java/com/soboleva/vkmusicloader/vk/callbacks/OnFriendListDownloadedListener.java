package com.soboleva.vkmusicloader.vk.callbacks;

import com.soboleva.vkmusicloader.vk.models.friends.Friend;

import java.util.List;

public interface OnFriendListDownloadedListener {
    public void onFriendListDownloaded(List<Friend> friends, int totalCount);

    public void onError();
}
