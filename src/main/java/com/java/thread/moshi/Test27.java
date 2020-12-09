package com.java.thread.moshi;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author ljw
 * @Date 2020/12/3 9:43
 * @Version 1.0
 * desc:设计模式之交替输出
 */
public class Test27 {

    public static void main(String[] args) {
        WaitNotify wn = new WaitNotify(1, 5);
        new Thread(()->{
            wn.print("a",1,2);
        }).start();
        new Thread(()->{
            wn.print("b",2,3);
        }).start();
        new Thread(()->{
            wn.print("c",3,1);
        }).start();
    }

}

class WaitNotify{

    private int flag;
    private int loopNumber;

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    public void print(String str, int waitFlag, int nextFlag){
        for(int i = 0; i<loopNumber; i++){
            synchronized (this){
                while (flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }

}

/******************************ReentrantLock方式*******************************************/
class test30{
    public static void main(String[] args) {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();

        new Thread(()->{
            awaitSignal.print("a",a,b);
        },"a").start();

        new Thread(()->{
            awaitSignal.print("b",b,c);
        },"a").start();

        new Thread(()->{
            awaitSignal.print("c",c,a);
        },"a").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        awaitSignal.lock();
        try {
            System.out.println("开始");
            a.signal();
        }finally {
            awaitSignal.unlock();
        }

    }
}

class AwaitSignal extends ReentrantLock{
    private int loopNumber;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Condition current, Condition next){
        for(int i = 0; i<loopNumber; i++){
            super.lock();
            try{
                current.await();//启动线程后，都在这里等待唤醒，唤醒后直接往下运行。
                System.out.print(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                super.unlock();
            }
        }
    }
}

/***********************************park/unpark方式*********************************/

class Test31{
    static Thread t1;
    static Thread t2;
    static Thread t3;

    public static void main(String[] args) {
        ParkUnpark pu = new ParkUnpark(5);
        t1 = new Thread(()->{
            pu.print("a",t2);
        });
        t2 = new Thread(()->{
            pu.print("b",t3);
        });
        t3 = new Thread(()->{
            pu.print("c",t1);
        });
        t1.start();
        t2.start();
        t3.start();

        LockSupport.unpark(t1);
    }

}

class ParkUnpark{

    private int loopNumber;

    public ParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Thread next){
        for(int i = 0; i<loopNumber; i++){
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(next);
        }
    }
}