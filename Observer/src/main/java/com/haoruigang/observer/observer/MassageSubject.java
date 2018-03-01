package com.haoruigang.observer.observer;

/**
 * 主题实现类
 * Created by 13739 on 2018/1/8.
 */

public class MassageSubject extends Subject {


    public void onClick(String state) {
        notifyObservers(state);
    }
}
