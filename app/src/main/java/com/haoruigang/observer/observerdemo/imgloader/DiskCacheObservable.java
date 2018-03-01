package com.haoruigang.observer.observerdemo.imgloader;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 13739 on 2018/1/15.
 * 2、硬盘缓存
 */

public class DiskCacheObservable extends CacheObservable {

    private DiskLruCache mDiskLruCache;
    private Context mContext;

    public DiskCacheObservable(Context context) {
        this.mContext = context;
        initDiskLruCache();
    }

    @Override
    public ImageBean getDataFromCache(String url) {
        Bitmap bitmap = getDataFromDiskLruCache(url);
        if (bitmap != null) {
            return new ImageBean(url, bitmap);
        }
        return null;
    }

    @Override
    public void putDataToCache(final ImageBean image) {
        //由于网络读取需要在子线程中执行
        Observable.create(new ObservableOnSubscribe<ImageBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ImageBean> e) throws Exception {
                putDataToDiskLruCache(image);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    /**
     * 初始化DiskLrucache
     */
    public void initDiskLruCache() {
        try {
            File cacheDir = getDiskCacheDir(mContext, "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(mContext), 1, 20 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取内存地
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取当前App版本
     *
     * @param context
     * @return
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取文件缓存
     *
     * @param url
     * @return
     */
    private Bitmap getDataFromDiskLruCache(String url) {
        Bitmap bitmap = null;
        FileDescriptor fileDescriptor = null;
        FileInputStream fileInputStream = null;
        try {
            final String key = hashKeyForDisk(url);
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                fileInputStream = (FileInputStream) snapshot.getInputStream(0);
                fileDescriptor = fileInputStream.getFD();
            }
            if (fileDescriptor != null) {
                bitmap = BitmapFactory.decodeStream(fileInputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    /**
     * 缓存文件数据
     *
     * @param imageBean
     */
    private void putDataToDiskLruCache(ImageBean imageBean) {
        try {
            String key = hashKeyForDisk(imageBean.getUrl());
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                boolean isSuccess = downloadUrlToStream(imageBean.getUrl(), outputStream);
                if (isSuccess) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @param urlString
     * @param outputStream
     * @return
     */
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
