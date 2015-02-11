package com.soboleva.vkmusicapp;

public class AccountManager {
    private static AccountManager sInstance;
    private String mAccessToken;
    private long mUserId;

    private AccountManager() {
    }

    public static synchronized AccountManager getInstance() {
        if (sInstance == null) {
            sInstance = new AccountManager();
        }
        return sInstance;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public long getUserId() {
        return mUserId;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }
}
