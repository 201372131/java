package com.java.thread.gongxiangmoxing;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Author ljw
 * @Date 2020/12/4 11:26
 * @Version 1.0
 * desc: 避免ABA问题
 */
public class Test36 {

    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("main start.....");
        //获取值A
        String prev = ref.getReference();
        //获取版本号
        int stamp = ref.getStamp();
        System.out.println("版本号："+stamp);
        //如果中间有其他线程干扰，发生了ABA现象
        other();
        Thread.sleep(1000);

        //尝试改为C
        System.out.println("change A->C :"+ref.compareAndSet(prev, "C", stamp, stamp+1));
    }

    private static void other() throws InterruptedException {
        new Thread(()->{
            System.out.println("change A-> B: "+ref.compareAndSet(ref.getReference(), "B", ref.getStamp(), ref.getStamp()+1));
            System.out.println("更新版本为："+ref.getStamp());
        },"t1").start();
        Thread.sleep(500);

        new Thread(()->{
            System.out.println("change B-> A: "+ref.compareAndSet(ref.getReference(), "A", ref.getStamp(), ref.getStamp()+1));
            System.out.println("更新版本为："+ref.getStamp());
        },"t2").start();
    }
}
