package com.java.thread.pool;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author: ljw
 * @Date: 2020/12/11 18:09
 * @Version: 1.0
 * desc:
 */
public class TestShutDown {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);

                Future<String> futures = pool.submit(() -> {
                    System.out.println("thread1 ---- begin");
                    Thread.sleep(1000);
                    System.out.println("thread1 ---- end");
                    return "1";
                });
                Future<String> futures2 = pool.submit(() -> {
                    System.out.println("thread1 ---- begin");
                    Thread.sleep(1000);
                    System.out.println("thread1 ---- end");
                    return "1";
                });
                Future<String> futures3 = pool.submit(() -> {
                    System.out.println("thread1 ---- begin");
                    Thread.sleep(1000);
                    System.out.println("thread1 ---- end");
                    return "1";
                });

        System.out.println("shutdown");
        //pool.shutdown();
        pool.shutdownNow();
        System.out.println("other");



    }
}
