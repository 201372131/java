package com.java.thread.reentranLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author ljw
 * @Date 2020/12/2 15:18
 * @Version 1.0
 * desc:ReentrantLock 锁超时，防止一直等待造成死锁
 */
public class ReentrantLock2 {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            System.out.println("尝试获得锁");
            try {
                if (!lock.tryLock(2, TimeUnit.SECONDS)) {//等待2秒
                    System.out.println("获取不到锁");
                    return;
                }
            } catch (InterruptedException e) {//支持可打断
                e.printStackTrace();
                System.out.println("获取不到锁");
                return;
            }
            try{
                System.out.println("获取到锁");
            }finally {
                lock.unlock();
            }
        },"t1");
        lock.lock();
        System.out.println("主线程获取到锁");
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
        System.out.println("主线程释放锁");
    }

}
