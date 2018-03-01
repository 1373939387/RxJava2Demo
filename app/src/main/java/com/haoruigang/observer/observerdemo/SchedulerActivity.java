package com.haoruigang.observer.observerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Scheduler线程控制
 */
public class SchedulerActivity extends AppCompatActivity {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.text)
    TextView text;
    API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        ButterKnife.bind(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://baike.baidu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(API.class);
    }

    @OnClick(R.id.button)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:

                Observable.create(new ObservableOnSubscribe<Result>() {
                    @Override
                    public void subscribe(ObservableEmitter<Result> e) throws Exception {

                        //请求network
                        Map<String, String> map = new HashMap<>();
                        map.put("scope", "103");
                        map.put("format", "json");
                        map.put("appid", "379020");
                        map.put("bk_key", "银魂");
                        map.put("bk_length", "600");
                        try {
                            Result result = api.getUserInfoQueryMap("openapi", "BaikeLemmaCardApi", map).execute().body();
//                        Info info = new Info(103, "json", 379020, "银魂", 600);
                            e.onNext(result);
                        } catch (Exception e1) {
                            e.onError(e1);
                        }


                    }
                }).subscribeOn(Schedulers.io())//Observable需要在子线程中执行,必须添加Schedulers.io()切换到子线程中
                        .observeOn(AndroidSchedulers.mainThread())//Observer需要在主线程中执行,当Schedulers.io()切换子线程的同时将Observer也切换成了子线程,所以需要使用AndroidSchedulers.mainThread()将Observer重新切换到主线程
                        .subscribe(new Observer<Result>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Result result) {

                                Log.v("SchedulerActivity", "" + result.toString());
                                text.setText(result.toString());

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
        }
    }
}
