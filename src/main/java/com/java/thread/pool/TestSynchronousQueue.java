package com.java.thread.pool;

import java.util.concurrent.SynchronousQueue;

/**
 * @Author: ljw
 * @Date: 2020/12/11 17:21
 * @Version: 1.0
 * desc:
 */
public class TestSynchronousQueue {

    public static void main(String[] args) {
        SynchronousQueue<Integer> integers = new SynchronousQueue<>();
        new Thread(() -> {
            try {
                System.out.println("t1-------putting {} "+ 1);
                integers.put(1);
                System.out.println("t1-------putted {} "+ 1);

                System.out.println("t1-------putting {} "+ 2);
                integers.put(2);
                System.out.println("t1-------putted {} "+ 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                System.out.println("t2-------taking {} "+ 1);
                integers.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2").start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                System.out.println("t3-------taking {} "+ 2);
                integers.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t3").start();
    }
}
