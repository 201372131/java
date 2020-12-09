package com.java.thread.reentranLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author ljw
 * @Date 2020/12/2 15:57
 * @Version 1.0
 * desc:条件变量，休息室，区分不同的等待区，房间，
 */
public class ReentrantLock4 {
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        //创建一个新的条件变量（休息室）
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        lock.lock();
        //进入休息室等待
        try {
            condition1.await();//await前需要先获取锁，await后会释放锁，进入conditionObject等待
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        condition1.signal();
        condition1.signalAll();

    }
}
