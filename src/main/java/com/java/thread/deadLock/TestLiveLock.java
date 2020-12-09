package com.java.thread.deadLock;

/**
 * @Author ljw
 * @Date 2020/12/2 14:43
 * @Version 1.0
 * desc:活锁
 * 解决活锁办法就是让他们的执行时间有一定的交错
 */
public class TestLiveLock {
    static volatile int count = 10;
    static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(()->{
            //期望减到0 退出循环
            while(count >0){
                try {
                    Thread.sleep(200);
                    count --;
                    System.out.println("t1--count:"+count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1").start();

        new Thread(()->{
            //期望超过20 退出循环
            while(count <20){
                try {
                    Thread.sleep(200);
                    count ++;
                    System.out.println("t2--count:"+count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t2").start();
    }
}
