package com.haoruigang.observer.observerdemo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.haoruigang.observer.observerdemo.RxImageLoader.Loader.RxImageLoader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.button2)
    Button button2;
    API api;
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://baike.baidu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        api = retrofit.create(API.class);

        //2、RxJava关键词搜索
//        RxBinding.tochange(控件);

        //3、RxJava防止按钮重复点击


        //4、RxJava合并本地和网络数据
//        合并函数方法

        //5、RxJava发送验证码
        //传统的倒计时
        new CountDownTimer(60, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

            }
        };

        //毕加索图片缓存框架
//        Picasso.with(this).load("http://car1.autoimg.cn/upload/2014/2/3/201402031932373813450.jpg").into(imageView);
        //自定义图片缓存框架
        RxImageLoader.with(this).load("http://lofter.nos.netease.com/sogou-NHVQclhfS2V6bG1DcTQ5elVjb1JoODVKUFpFUUstNVUwNWhTMVgxQWIzVWV4YlZPcnhkcHBkRWdpN183NEZLWQ.jpg").into(imageView);
    }

    private Info getInfoParam() {
        Info info = new Info(103, "json", 379020, "银魂", 600);
        return info;
    }

    @OnClick(R.id.button2)
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.button2:
                //1、RxJava登录后获取用户信息
                Observable.just(getInfoParam()).flatMap(new Function<Info, ObservableSource<Result>>() {

                    @Override
                    public ObservableSource<Result> apply(Info info) throws Exception {
//                        Result result = api.savaUser(info).execute().body();
//                        return Observable.just(result);
                        return api.savaUser(info);//访问网络
                    }
                }).flatMap(new Function<Result, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(Result result) throws Exception {
                        //通过result返回作为条件请求网络
                        return Observable.just(new User(result.getUrl(), result.getWapUrl()));
                    }
                }).subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Exception {
                                Toast.makeText(LoginActivity.this, user.getUsername() + ";" +
                                        user.getPassword(), Toast.LENGTH_LONG).show();
                            }
                        });
                //RxJava倒计时
                CountDownTimerTime();
                break;
        }
    }

    /**
     * RxJava倒计时
     */
    public void CountDownTimerTime() {
        final int count = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return count - aLong;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        button2.setEnabled(false);
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long value) {
                        Log.d("LoginActivity", "onNext" + value);
                        button2.setText(value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("LoginActivity", "onComplete");
                        button2.setEnabled(true);
                        button2.setText("发送验证码");
                    }
                });
    }

}
