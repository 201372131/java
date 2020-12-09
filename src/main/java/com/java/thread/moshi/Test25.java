package com.java.thread.moshi;

/**
 * @Author ljw
 * @Date 2020/12/3 9:24
 * @Version 1.0
 * desc:顺序控制
 */
public class Test25 {
    static final Object lock = new Object();
    static boolean t2runned = false;

    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            synchronized (lock){
                while (!t2runned){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("1");
            }
        },"t1");

        Thread t2 = new Thread(()->{
            synchronized (lock){
                System.out.println("2");
                t2runned = true;
                lock.notify();
            }
        },"t2");

        t1.start();
        t2.start();
    }
}
