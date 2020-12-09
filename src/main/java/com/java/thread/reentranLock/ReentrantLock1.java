package com.java.thread.reentranLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author ljw
 * @Date 2020/12/2 15:06
 * @Version 1.0
 * desc:ReentrantLock锁可打断性
 */
public class ReentrantLock1 {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            try {
                //如果没有竞争那么此方法就会获取lock对象锁
                //如果有竞争就进入阻塞队列，可以被其他线程用interrupt方法打断不在等待。
                System.out.println("尝试获得锁");
                lock.lockInterruptibly();//lock.lock()方法无法打断
            }catch (InterruptedException e){
                e.printStackTrace();
                System.out.println("没有获得锁，返回");
                return;
            }
            try{
                System.out.println("获取到锁");
            }finally {
                lock.unlock();
            }
        },"t1");
        lock.lock();
        t1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("打断 t1");
        t1.interrupt();
    }
}
