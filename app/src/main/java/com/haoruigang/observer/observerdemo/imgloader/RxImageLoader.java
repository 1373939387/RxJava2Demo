package com.haoruigang.observer.observerdemo.imgloader;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by 13739 on 2018/1/15.
 * RxJava2三级图片缓存框架
 */

public class RxImageLoader {

    static RxImageLoader singleton;
    private String mUrl;
    private RequestCreator mRequestCreator;

    private RxImageLoader(Builder builder) {
        mRequestCreator = new RequestCreator(builder.context);
    }

    public static RxImageLoader with(Context context) {
        if (singleton == null) {
            synchronized (RxImageLoader.class) {
                if (singleton == null) {
                    singleton = new Builder(context).build();
                }
            }
        }
        return singleton;
    }

    public RxImageLoader load(String url) {
        this.mUrl = url;
        return singleton;
    }

    public void into(final ImageView imageView) {
        Observable.concat(
                mRequestCreator.getImageFromMemory(mUrl),
                mRequestCreator.getImageFromDisk(mUrl),
                mRequestCreator.getImageFromNetWork(mUrl))
                .first(new ImageBean(mUrl, null)).toObservable()
                .subscribe(new Observer<ImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("onSubscribe", "onSubscribe");
                    }

                    @Override
                    public void onNext(ImageBean imageBean) {
                        if (imageBean.getmBitmap() != null) {
                            imageView.setImageBitmap(imageBean.getmBitmap());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.e("onComplete", "onComplete");
                    }
                });
    }

    public static class Builder {

        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public RxImageLoader build() {
            return new RxImageLoader(this);
        }
    }
}
