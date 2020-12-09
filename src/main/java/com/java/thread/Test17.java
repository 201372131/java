package com.java.thread;

/**
 * @Author ljw
 * @Date 2020/11/30 16:28
 * @Version 1.0
 * synchronized基本使用，不用的话结果可能不为0
 * 当一个线程还拿着锁时候，时间片用完了，该线程也会停止执行不会释放锁，
 */
public class Test17 {


    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();

        Thread t1 = new Thread(()->{
            for(int i = 0; i<5000; i++){
                room.increment();
            }
        },"t1");

        Thread t2 = new Thread(()->{
            for(int i =0; i<5000; i++){
                room.decrement();
            }
        },"t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(room.getCounter());
    }

    //面向对象思想
    static class Room{
        private int counter = 0;

        public void increment(){
            synchronized (this){//锁的是调用该方法的对象
                counter++;
            }
        }

        public void decrement(){
            synchronized (this){
                counter--;
            }
        }

        public int getCounter(){
            synchronized (this){
                return counter;
            }
        }

    }
}
