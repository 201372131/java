package com.java.thread;


/**
 * @Author ljw
 * @Date 2020/11/27 14:37
 * @Version 1.0
 */

public class Demo2 {

    //@FunctionalInterface  可以用lamda
    public static void main(String[] args) {
        Runnable r = () -> {
            System.out.println("runnable启动了");
        };

        Thread t = new Thread(r,"t2");
        t.start();
    }

}
