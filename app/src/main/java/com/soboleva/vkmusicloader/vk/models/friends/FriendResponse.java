package com.soboleva.vkmusicloader.vk.models.friends;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendResponse {

    @SerializedName("count")
    private int mCount;

    @SerializedName("items")
    private List<Friend> mFriendList;

    public List<Friend> getFriendList() {
        return mFriendList;
    }

    public int getTotalCount() {
        return mCount;
    }

    @Override
    public String toString() {
        return "FriendResponse{" +
                "mCount=" + mCount +
                ", mFriendList=" + mFriendList +
                '}';
    }
}
