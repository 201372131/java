package com.java.thread.gongxiangmoxing;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

/**
 * @Author ljw
 * @Date 2020/12/4 10:25
 * @Version 1.0
 * desc:
 */
public class Test34 {

    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger(0);//原子操作不会有线程安全问题
        System.out.println(i.incrementAndGet());//++i
        System.out.println(i.getAndIncrement());//i++   先打印1 然后自增1  ==2
        System.out.println(i.get());//2
        System.out.println("================================");

        System.out.println(i.getAndAdd(5));//2  先获取再加5
        System.out.println(i.addAndGet(5));//12
        System.out.println("=================================");

        System.out.println(i.updateAndGet(x -> x*10));//120 先运算再获取   x:读取到的值     x*10:要设置的值

        System.out.println(updateAndGet(i, p -> p / 2));

    }

    //实现原理
    public static int updateAndGet(AtomicInteger i, IntUnaryOperator operator){
        while (true){
            int prev = i.get();
            int next = operator.applyAsInt(prev);
            if(i.compareAndSet(prev,next)){
                return next;
            }
        }
    }

}
