package com.haoruigang.observer.observerdemo.imgloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 13739 on 2018/1/15.
 * 3、网络缓存
 */

public class NetWorkCacheObservable extends CacheObservable {
    @Override
    public ImageBean getDataFromCache(String url) {
        Bitmap bitmap = downloadImage(url);
        if (bitmap != null) {
            return new ImageBean(url, bitmap);
        }
        return null;
    }

    @Override
    public void putDataToCache(ImageBean image) {

    }

    private Bitmap downloadImage(String url) {
        return getBitmap(url);
    }

    /**
     * 从互联网上获取原始大小图片.
     *
     * @param url 要下载文件的网络地址
     * @return Bitmap 新图片
     */
    public static Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        URLConnection con = null;
        InputStream is = null;
        try {
            URL imageURL = new URL(url);
            con = imageURL.openConnection();
            con.setDoInput(true);
            con.connect();
            is = con.getInputStream();
            // 获取资源图片
            bitmap = BitmapFactory.decodeStream(is, null, null);
        } catch (Exception e) {
            Log.d("NetWorkCacheObservable", "" + e.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
