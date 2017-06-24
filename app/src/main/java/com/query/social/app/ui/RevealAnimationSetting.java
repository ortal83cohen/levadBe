package com.query.social.app.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class RevealAnimationSetting implements Parcelable {
    private int height;
    private int width;
    private int centerY;
    private int centerX;
    private int fabColor;
    private int pageColor;


    protected RevealAnimationSetting(Parcel in) {
    }

    public static final Creator<RevealAnimationSetting> CREATOR = new Creator<RevealAnimationSetting>() {
        @Override
        public RevealAnimationSetting createFromParcel(Parcel in) {
            return new RevealAnimationSetting(in);
        }

        @Override
        public RevealAnimationSetting[] newArray(int size) {
            return new RevealAnimationSetting[size];
        }
    };

    public RevealAnimationSetting(int centerX, int centerY, int width, int height, int fabColor, int pageColor) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.height = height;
        this.width = width;
        this.fabColor = fabColor;
        this.pageColor = pageColor;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFabColor() {
        return fabColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static RevealAnimationSetting with(int centerX, int centerY, int width, int height, int fabColor,int pageColor) {
        return new RevealAnimationSetting(centerX, centerY, width, height, fabColor,pageColor);
    }

    public int getPageColor() {
        return pageColor;
    }
}
