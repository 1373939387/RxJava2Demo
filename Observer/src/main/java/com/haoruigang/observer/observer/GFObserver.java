package com.haoruigang.observer.observer;

/**
 * 观察者实现类
 * Created by 13739 on 2018/1/8.
 */

public class GFObserver implements Observer {

    public String getName() {
        return name;
    }

    private String name;

    public GFObserver(String name) {
        super();
        this.name = name;
    }

    @Override
    public void update(String state) {
        System.out.println(this.getName() + " 接收到新消息，男朋友最近状态：" + state);
    }
}
