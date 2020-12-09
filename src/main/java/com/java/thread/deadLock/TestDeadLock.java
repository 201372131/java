package com.java.thread.deadLock;

/**
 * @Author ljw
 * @Date 2020/12/2 14:08
 * @Version 1.0
 * desc:死锁
 */
public class TestDeadLock {

    public static void main(String[] args) {
        test1();
    }

    private static void test1(){
        Object A = new Object();
        Object B = new Object();

        Thread t1 = new Thread(()->{
            System.out.println("lock A");
            synchronized (A){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B){
                    System.out.println("lock B");
                    System.out.println("操作。。。。");
                }
            }
        },"t1");

        Thread t2 = new Thread(()->{
            System.out.println("lock B");
            synchronized (B){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (A){
                    System.out.println("lock A");
                    System.out.println("操作。。。。");
                }
            }
        },"t2");

        t1.start();
        t2.start();
    }
}
