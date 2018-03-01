package com.haoruigang.observer.observerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_rxjava)
    Button button;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.btn_scheduler)
    Button btnScheduler;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.editText)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_rxjava, R.id.btn_scheduler, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_rxjava:

                //第二种简写Consumer<Object>代替了Observer
//        getObservable().subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                text.setText(s+"===");
//            }
//        });
                //第一种
                getObservable().subscribe(getObserver());
                break;
            case R.id.btn_scheduler:
                startActivity(new Intent(MainActivity.this, SchedulerActivity.class));
                break;
            case R.id.btn_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
        }
    }

    /**
     * 创建被观察者（主体）
     *
     * @return
     */
    public Observable<String> getObservable() {
        //第二种简写的几种方法
//        return Observable.just("李小璐", "贾乃亮", "绿帽子");
//        Observable.fromArray("李小璐", "贾乃亮", "绿帽子");
//        Observable.fromCallable(new Callable<Object>() {
//            @Override
//            public Object call() throws Exception {
//                return "范冰冰";
//            }
//        });
        //第一种写法
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                try {
                    e.onNext("李小璐");
                    e.onNext("出轨了");
                    //e.onError();//和onError()不能同时调用（同时调用会执行前一个）
                } catch (Exception e1) {
                    e.onComplete();
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * 创建观察者
     */
    public Observer<String> getObserver() {
        return new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d("MainActivity", "onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d("MainActivity", "onNext");
                text.setText(s + "===");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("MainActivity", "onError");
            }

            @Override
            public void onComplete() {
                Log.d("MainActivity", "onComplete");
            }
        };
    }

}
