package com.haoruigang.observer.observerdemo.imgloader;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 13739 on 2018/1/15.
 * 缓存抽象
 * 主类
 */

public abstract class CacheObservable {

    public Observable<ImageBean> getImage(final String url) {
        return Observable.create(new ObservableOnSubscribe<ImageBean>() {
            @Override
            public void subscribe(ObservableEmitter<ImageBean> e) throws Exception {
                if (!e.isDisposed()) {
                    ImageBean image = getDataFromCache(url);
                    e.onNext(image);
                    e.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 读取数据
     *
     * @param url
     * @return
     */
    public abstract ImageBean getDataFromCache(String url);

    /**
     * 放入缓存
     *
     * @param image
     */
    public abstract void putDataToCache(ImageBean image);
}
