package com.soboleva.vkmusicapp.api.vk.models.friends;

import com.google.gson.annotations.SerializedName;
import com.soboleva.vkmusicapp.api.vk.models.BaseData;

import java.io.Serializable;

public class Friend extends BaseData implements Serializable {
    @SerializedName("id")
    private String mID;

    @SerializedName("first_name")
    private String mFirstName;

    @SerializedName("last_name")
    private String mLastName;

    @SerializedName("photo_100")
    private String mPhoto100;

    @SerializedName("photo_200_orig")
    private String mPhoto200;

    @SerializedName("online")
    private String mOnline;

    public String getID() {
        return mID;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getOnline() {
        return mOnline;
    }

    public String getPhoto100() {
        return mPhoto100;
    }

    public String getPhoto200() {
        return mPhoto200;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "mID='" + mID + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mPhoto100='" + mPhoto100 + '\'' +
                ", mPhoto200='" + mPhoto200 + '\'' +
                ", mOnline='" + mOnline + '\'' +
                '}';
    }
}
