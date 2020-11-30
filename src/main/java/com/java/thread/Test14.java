package com.java.thread;

import sun.rmi.runtime.Log;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author ljw
 * @Date 2020/11/30 10:50
 * @Version 1.0
 * desc:park()打断
 * interrupted():可以清除打断标识
 */
public class Test14 {

    public static void main(String[] args) throws InterruptedException {
        test3();
    }

    private static void test3() throws InterruptedException {
        Thread t1 = new Thread(()->{
            System.out.println("park...");
            LockSupport.park();//打断标记为true，后面再有park就会失效
            System.out.println("unPark....");
            //System.out.println("打断状态 "+Thread.currentThread().isInterrupted());
            System.out.println("打断状态："+Thread.interrupted());//interrupted()该方法会清除打断标记设置为false
            LockSupport.park();
            System.out.println("unPark......");
        },"t1");
        t1.start();

        Thread.sleep(1000);
        t1.interrupt();//可以打断park() 让park后面的代码继续运行
    }
}
