package com.haoruigang.observer.observerdemo.imgloader;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by 13739 on 2018/1/15.
 */

public class RequestCreator {

    private MemoryCacheObservable mMemoryCacheObservable;
    private DiskCacheObservable mDiskCacheObservable;
    private NetWorkCacheObservable mNetWorkCacheObservable;

    public RequestCreator(Context context) {
        super();
        mMemoryCacheObservable = new MemoryCacheObservable();
        mDiskCacheObservable = new DiskCacheObservable(context);
        mNetWorkCacheObservable = new NetWorkCacheObservable();
    }

    public Observable<ImageBean> getImageFromMemory(String url) {

        return mMemoryCacheObservable.getImage(url)
                .filter(new Predicate<ImageBean>() {
                    @Override
                    public boolean test(ImageBean imageBean) throws Exception {
                        return imageBean != null;
                    }
                });
    }

    public Observable<ImageBean> getImageFromDisk(String url) {

        return mDiskCacheObservable.getImage(url)
                .filter(new Predicate<ImageBean>() {
                    @Override
                    public boolean test(ImageBean imageBean) throws Exception {
                        return imageBean != null;
                    }
                }).doOnNext(new Consumer<ImageBean>() {
                    @Override
                    public void accept(ImageBean imageBean) throws Exception {
                        mMemoryCacheObservable.putDataToCache(imageBean);
                    }
                });
    }

    public Observable<ImageBean> getImageFromNetWork(String url) {

        return mNetWorkCacheObservable.getImage(url)
                .filter(new Predicate<ImageBean>() {
                    @Override
                    public boolean test(ImageBean imageBean) throws Exception {
                        return imageBean != null;
                    }
                })
                .doOnNext(new Consumer<ImageBean>() {
                    @Override
                    public void accept(ImageBean imageBean) throws Exception {
                        mDiskCacheObservable.putDataToCache(imageBean);
                        mMemoryCacheObservable.putDataToCache(imageBean);
                    }
                });
    }

}
