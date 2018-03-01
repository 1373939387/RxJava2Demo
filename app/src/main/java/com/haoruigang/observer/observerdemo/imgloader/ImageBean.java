package com.haoruigang.observer.observerdemo.imgloader;

import android.graphics.Bitmap;

/**
 * Created by 13739 on 2018/1/15.
 */

public class ImageBean {

    private String url;
    private Bitmap mBitmap;

    public ImageBean(String url, Bitmap mBitmap) {
        this.url = url;
        this.mBitmap = mBitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }
}
