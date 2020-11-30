package com.java.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author ljw
 * @Date 2020/11/27 16:05
 * @Version 1.0
 */
public class Demo3 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {

                System.out.println("running.....");
                Thread.sleep(2000);
                return 100;
            }
        });

        Thread t = new Thread(task,"t1");
        t.start();

        Integer integer = task.get();//阻塞等待
        System.out.println(integer);

    }
}
