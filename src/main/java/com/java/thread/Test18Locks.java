package com.java.thread;

/**
 * @Author ljw
 * @Date 2020/11/30 17:18
 * @Version 1.0
 * desc:线程八锁
 * n1.a()和n1.b() 因为n1对象一样，所以a和b方法锁住的this都是Number，所以他们同一时间只有一个线程拿到锁。
 */
public class Test18Locks {

    public static void main(String[] args) {
        Number n1 = new Number();
        new Thread(()->{
            System.out.println("线程一开始");
            n1.a();
        }).start();

        new Thread(()->{
            System.out.println("线程二开始");
            n1.b();
        }).start();


    }
}

class Number{

    public synchronized void a(){
        System.out.println("1");
    }

    public synchronized void b(){
        System.out.println("2");
    }
}
