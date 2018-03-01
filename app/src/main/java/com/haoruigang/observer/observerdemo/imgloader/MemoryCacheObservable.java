package com.haoruigang.observer.observerdemo.imgloader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by 13739 on 2018/1/15.
 * 1、内存缓存
 */

public class MemoryCacheObservable extends CacheObservable {

    int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    int cacheSize = maxMemory / 8;

    LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }
    };

    @Override
    public ImageBean getDataFromCache(String url) {
        Bitmap bitmap = mLruCache.get(url);
        if (bitmap != null) {
            return new ImageBean(url, bitmap);
        }
        return null;
    }

    @Override
    public void putDataToCache(ImageBean image) {
        mLruCache.put(image.getUrl(), image.getmBitmap());
    }
}
