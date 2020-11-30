package com.java.thread;


/**
 * @Author ljw
 * @Date 2020/11/30 11:42
 * @Version 1.0
 * desc:主线程和守护线程，
 * 守护线程等待其他非守护线程结束后就结束，即使自己的代码没有运行完。
 * 垃圾回收器就属于守护线程
 */
public class Test15 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            while (true){
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
            }
            System.out.println("结束");
        },"t1");
        t1.setDaemon(true);//设置为守护线程
        t1.start();

        Thread.sleep(1000);
        System.out.println("结束");
    }
}
