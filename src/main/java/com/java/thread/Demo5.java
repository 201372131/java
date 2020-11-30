package com.java.thread;

/**
 * @Author ljw
 * @Date 2020/11/27 16:57
 * @Version 1.0
 * desc:栈演示，后进先出，方法执行完后就会释放内存。
 */
public class Demo5 {

    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @Override
            public void run(){
                method1(20);
            }
        };
        t1.setName("t1");
        t1.start();
        method1(10);
    }

    private static void method1(int x) {
        int y = x +1;
        Object m = method2();
        System.out.println(m);
    }

    private static Object method2() {
        Object n = new Object();
        return n;
    }
}
