package com.java.thread.jmm;

/**
 * @Author ljw
 * @Date 2020/12/3 14:05
 * @Version 1.0
 * desc:停不下来的代码
 * 内存可见性，main主线程修改了变量，t线程不可见
 */
public class Test32 {
    static boolean run = true;//volatile易变

    final static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            while (true){
                synchronized (lock){
                    if(!run){
                        break;
                    }
                }

            }
        });
        t.start();
        Thread.sleep(1000);
        System.out.println("停止");
        synchronized (lock){
            run = false;
        }
    }
}
