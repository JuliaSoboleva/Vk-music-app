package com.soboleva.vkmusicapp.api.vk.models.friends;

import com.google.gson.annotations.SerializedName;

public class FriendResponseModel {
    @SerializedName("response")
    private FriendResponse mFriendResponse;

    public FriendResponse getFriendResponse() {
        return mFriendResponse;
    }

    @Override
    public String toString() {
        return "FriendResponseModel{" +
                "mFriendResponse=" + mFriendResponse +
                '}';
    }
}
