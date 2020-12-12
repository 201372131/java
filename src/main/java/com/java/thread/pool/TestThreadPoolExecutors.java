package com.java.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: ljw
 * @Date: 2020/12/11 17:07
 * @Version: 1.0
 * desc:
 */
public class TestThreadPoolExecutors {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        pool.execute(()->{
            System.out.println("1");
        });

        pool.execute(()->{
            System.out.println("2");
        });

        pool.execute(()->{
            System.out.println("3");
        });
    }
}
