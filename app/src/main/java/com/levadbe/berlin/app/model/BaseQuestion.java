package com.levadbe.berlin.app.model;

/**
 * Created by Ortal Cohen on 24/6/2017.
 */

public class BaseQuestion {
    String mUid;
    String mHeader;
    String mUserId;
    String mUserName;

    public BaseQuestion() {
    }

    public BaseQuestion(String mUid, String mHeader, String mUserId, String mUserName) {
        this.mHeader = mHeader;
        this.mUid = mUid;
        this.mUserId = mUserId;
        this.mUserName = mUserName;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public String getmHeader() {
        return mHeader;
    }

    public void setmHeader(String mHeader) {
        this.mHeader = mHeader;
    }
}
