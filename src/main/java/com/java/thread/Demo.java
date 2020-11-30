package com.java.thread;

/**
 * @Author ljw
 * @Date 2020/11/27 14:32
 * @Version 1.0
 */
public class Demo {

    public static class MyThread extends Thread{
        @Override
        public void run(){
            System.out.println("MyThread");
        }
    }

    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();//不可多次调用start()方法，第二次调用会抛出异常

        Thread thread = new Thread(){
            @Override
            public void run(){
                System.out.println("sdfsdf");
            }
        };
        thread.setName("t1");//指定线程名字
        thread.start();
    }


}
