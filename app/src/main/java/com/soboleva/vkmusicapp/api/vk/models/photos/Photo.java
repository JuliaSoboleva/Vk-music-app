package com.soboleva.vkmusicapp.api.vk.models.photos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Photo implements Serializable {
    @SerializedName("photo_604")
    private String mPhoto604;

    public String getPhoto604() {
        return mPhoto604;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mPhoto604='" + mPhoto604 + '\'' +
                '}';
    }
}
