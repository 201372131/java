package com.java.thread;

/**
 * @Author ljw
 * @Date 2020/11/30 9:54
 * @Version 1.0
 */
public class Test10 {

    static int r=0;

    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    private static void test1() throws InterruptedException {
        System.out.println("开始");
        Thread t1 = new Thread(()-> {
            System.out.println("线程开始");
            try {
                Thread.sleep(1);
                r=10;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1");

        t1.start();
        //t1.join();//等待t1线程结束后再往下执行
        System.out.println("结果为："+r);

    }
}
