package com.java.thread.moshi;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author ljw
 * @Date 2020/12/3 9:38
 * @Version 1.0
 * desc:顺序控制之 park/unpark
 */
public class Test26 {

    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            LockSupport.park();
            System.out.println(1);
        },"t1");
        t1.start();

        new Thread(()->{
            System.out.println("2");
            LockSupport.unpark(t1);
        },"t2").start();
    }
}
