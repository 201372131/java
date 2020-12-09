package com.java.thread.reentranLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author ljw
 * @Date 2020/12/2 14:59
 * @Version 1.0
 * desc:ReentrantLock 可重入性
 */
public class test22 {

    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        lock.lock();
        try {
            System.out.println("enter main");
            m1();
        }finally {
            lock.unlock();
        }
    }

    public static void m1(){
        lock.lock();
        try {
            System.out.println("enter m1");
            m2();
        }finally {
            lock.unlock();
        }
    }

    public static void m2(){
        lock.lock();
        try {
            System.out.println("enter m2");
        }finally {
            lock.unlock();
        }
    }

}
