package com.soboleva.vkmusicapp.api.vk.models.photos;

import com.google.gson.annotations.SerializedName;

public class PhotoResponseModel {
    @SerializedName("response")
    private PhotoResponse mPhotoResponse;

    public PhotoResponse getPhotoResponse() {
        return mPhotoResponse;
    }

}
