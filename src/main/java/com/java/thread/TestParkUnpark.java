package com.java.thread;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author ljw
 * @Date 2020/12/2 11:45
 * @Version 1.0
 * desc:
 */
public class TestParkUnpark {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            System.out.println("start.....");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("park...");
            LockSupport.park();//wait状态，不唤醒一直等待
            System.out.println("resume......");
        },"t1");
        t1.start();

        Thread.sleep(1000);
        System.out.println("unpark.....");
        LockSupport.unpark(t1);//可以在park之前和之后调用有同样的效果
    }
}
