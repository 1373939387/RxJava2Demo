package com.haoruigang.observer.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 主题
 * Created by 13739 on 2018/1/8.
 */

public abstract class Subject {

    /**
     * 保存女朋友集合
     */
    private List<Observer> observers = new ArrayList<Observer>();


    /**
     * 添加女朋友
     *
     * @param observer
     */
    public void attach(Observer observer) {
        observers.add(observer);
        System.out.print("我新交了一个女朋友,叫"+observer.getName());
    }

    /**
     * 删除分手
     *
     * @param observer
     */
    public void dettch(Observer observer) {
        observers.remove(observer);
        System.out.print("我和她分手了");
    }

    /**
     * 通知所有女朋友更新状态
     *
     * @param state
     */
    public void notifyObservers(String state) {
        for (Observer observer : observers) {
            observer.update(state);
        }
    }

}
