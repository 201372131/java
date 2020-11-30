package com.java.thread;



/**
 * @Author ljw
 * @Date 2020/11/27 16:11
 * @Version 1.0
 */
public class Demo4 {

    public static void main(String[] args) {
        new Thread(()->{
            while (true){
                System.out.println("running......");
            }
        },"t1").start();

        new Thread(()-> {
            while (true){
                System.out.println("running..........");
            }
        },"t2").start();

    }
}
