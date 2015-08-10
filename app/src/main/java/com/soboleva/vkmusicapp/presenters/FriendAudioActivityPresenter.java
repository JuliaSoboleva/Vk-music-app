package com.soboleva.vkmusicapp.presenters;

import com.soboleva.vkmusicapp.api.vk.VkApi;
import com.soboleva.vkmusicapp.api.vk.callbacks.OnPhotoDownloadedListener;
import com.soboleva.vkmusicapp.api.vk.models.friends.Friend;
import com.soboleva.vkmusicapp.api.vk.models.photos.Photo;
import com.soboleva.vkmusicapp.ui.activities.FriendAudioActivity;
import timber.log.Timber;

public class FriendAudioActivityPresenter {

    private final FriendAudioActivity mFriendAudioActivity;
    private final Friend mFriend;
    private final VkApi mVkApi;


    private String mFriendPhoto;



    public FriendAudioActivityPresenter(FriendAudioActivity friendAudioActivity, Friend friend) {
        mFriendAudioActivity = friendAudioActivity;
        mVkApi = VkApi.getInstance();
        mFriend = friend;
    }


//    protected void showItems(int offset, List<Audio> data, int totalCount) {
//        mAvailableItemCount += data.size();
//        if (offset == 0) {
//            mFriendAudioActivity.showItems(data);
//            mTotalItemCount = totalCount;
//        } else {
//            mFriendAudioActivity.showWithAddedItems(data);
//        }
//        mIsDownloadingNow = false;
//    }

    public void getFriedPhoto() {

        mVkApi.getFriendPhoto(new OnPhotoDownloadedListener() {
            @Override
            public void OnFriendPhotoDownloaded(Photo photo) {
                mFriendPhoto = photo.getPhoto604();
                mFriendAudioActivity.showPhoto(mFriendPhoto);

            }

            @Override
            public void onError() {
                //no photo
                Timber.d("No fhoto");
                mFriendAudioActivity.hidePhoto();
            }
        }, mFriend.getID());
    }




}
