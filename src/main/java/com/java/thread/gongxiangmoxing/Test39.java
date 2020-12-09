package com.java.thread.gongxiangmoxing;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Author ljw
 * @Date 2020/12/4 15:04
 * @Version 1.0
 * desc:
 */
public class Test39 {

    public static void main(String[] args) {
        for(int i = 0; i<5; i++){
            demo(
                    ()->new LongAdder(),
                    adder -> adder.increment()
            );
        }

        for (int i = 0; i<5; i++){
            demo(
                    ()-> new AtomicLong(),
                    adder -> adder.getAndIncrement()
            );
        }
    }



    private static <T> void demo(Supplier<T> adderSupplier, Consumer<T> action){
        T adder = adderSupplier.get();
        long start = System.nanoTime();
        ArrayList<Thread> ts = new ArrayList<>();

        //四个线程，每人累加50万
        for(int i = 0; i<40; i++){
            ts.add(new Thread(()->{
                for(int j = 0; j<500000; j++){
                    action.accept(adder);
                }
            }));
        }

        ts.forEach(t -> t.start());
        ts.forEach(t ->{
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        long end = System.nanoTime();
        System.out.println(adder +" cost :"+(end - start)/1000_000);
    }
}


