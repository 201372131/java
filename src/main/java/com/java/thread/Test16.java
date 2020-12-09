package com.java.thread;


import java.sql.SQLOutput;

/**
 * @Author ljw
 * @Date 2020/11/30 15:52
 * @Version 1.0
 * desc:烧水泡茶
 */
public class Test16 {

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(()->{

            try {
                System.out.println("洗水壶");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("烧开水");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"老王");

        Thread t2 = new Thread(()->{
            try {
                System.out.println("洗茶壶");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("洗茶杯");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("拿茶叶");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"小王");

        t1.start();
        t2.start();

    }

}
