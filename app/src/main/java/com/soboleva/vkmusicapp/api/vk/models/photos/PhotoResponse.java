package com.soboleva.vkmusicapp.api.vk.models.photos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoResponse {
    @SerializedName("count")
    private int mCount;

    @SerializedName("items")
    private List<Photo> mPhotos;

    public Photo getPhoto() {
        return mPhotos.get(0);
    }

    public int getCount() {
        return mCount;
    }

    @Override
    public String toString() {
        return "PhotoResponse{" +
                "mCount=" + mCount +
                ", mPhotos=" + mPhotos +
                '}';
    }
}
