package com.haoruigang.observer.observer;

/**
 * Created by 13739 on 2018/1/8.
 */

public class Client {

    public static void main(String[] args) {
        Observer observer1 = new GFObserver("李小璐");
        Observer observer2 = new GFObserver("范冰冰");

        MassageSubject subject = new MassageSubject();

        subject.attach(observer1);
        subject.attach(observer2);

        subject.onClick("出轨了");
    }
}
