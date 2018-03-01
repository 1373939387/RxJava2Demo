package com.haoruigang.observer.observer;

/**
 * 观察者
 * Created by 13739 on 2018/1/8.
 */
public interface Observer {

    String getName();

    void update(String state);

}
