package com.java.thread.reentranLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author ljw
 * @Date 2020/12/2 16:05
 * @Version 1.0
 * desc:
 */
public class ReentrantLock5 {

    static boolean hasCigarette = false;
    static boolean hasTakeout = false;
    static ReentrantLock ROOM = new ReentrantLock();
    static Condition waitCigaretteSet = ROOM.newCondition();//等烟的休息室
    static Condition waitTakeoutSet = ROOM.newCondition();//等待外卖的休息室


    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            ROOM.lock();
            try {
                System.out.println("【小南】有烟吗？ "+hasCigarette);
                //while为true时，里面的代码块会再次执行不会退出循环，不会执行while代码块后面的代码。if条件被唤醒后从上次停止的地方往下执行，所以不会再次进入等待
                while (!hasCigarette){
                    System.out.println("【小南】没有烟，先歇会");
                    try {
                        waitCigaretteSet.await();//等烟的休息室
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("【小南】有烟吗？ "+hasCigarette);
                if(hasCigarette){
                    System.out.println("【小南】可以开始干活了");
                }else{
                    System.out.println("【小南】没干活");
                }
            }finally {
                ROOM.unlock();

            }
        },"小南").start();

        new Thread(()->{
            ROOM.lock();
            try {
                System.out.println("【小女】外卖到了没？ "+hasCigarette);
                if(!hasTakeout){
                    System.out.println("【小女】没外卖，先歇会");
                    try {
                        waitTakeoutSet.await();//等外卖的休息室
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("【小女】外卖到了没？ "+hasTakeout);
                if(hasTakeout){
                    System.out.println("【小女】可以开始干活了");
                }else{
                    System.out.println("【小女】没干活");
                }
            }finally {
                ROOM.unlock();
            }
        },"小女").start();


        Thread.sleep(1000);
        new Thread(()->{
            ROOM.lock();
            try {
                hasTakeout = true;
                waitTakeoutSet.signal();
            }finally {
                ROOM.unlock();
            }

        },"送外卖的").start();

        Thread.sleep(1000);
        new Thread(()->{
            ROOM.lock();
            try {
                hasCigarette = true;
                waitCigaretteSet.signal();
            }finally {
                ROOM.unlock();
            }

        },"送烟的").start();
    }
}
