package com.java.thread;

/**
 * @Author ljw
 * @Date 2020/11/27 17:42
 * @Version 1.0
 * desc； 测试优先级和yield效果
 * 他们只能参考不能绝对
 */
public class Demo6 {

    public static void main(String[] args) {
        Runnable task1 = () -> {
            int count = 0;
            for(;;){
                System.out.println("------>1"+count++);
            }
        };

        Runnable task2 = () -> {
            int count = 0;
            for(;;){
                Thread.yield();
                System.out.println("        -------2"+count++);
            }
        };

        Thread t1 = new Thread(task1,"t1");
        Thread t2 = new Thread(task2,"t2");
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();
    }
}
