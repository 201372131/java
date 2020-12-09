package com.java.thread.gongxiangmoxing;

import sun.rmi.runtime.Log;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @Author ljw
 * @Date 2020/12/4 14:22
 * @Version 1.0
 * desc:
 */
public class Test38 {

    public static void main(String[] args) throws InterruptedException {
        GarbageBag bag = new GarbageBag("装满了垃圾");

        AtomicMarkableReference<GarbageBag> ref = new AtomicMarkableReference<>(bag,true);

        System.out.println("主线程start。。。。");
        GarbageBag prev = ref.getReference();
        System.out.println(prev.toString());

        new Thread(()->{
            System.out.println("打扫卫生的线程 start。。。");
            bag.setDesc("空垃圾袋");
            while (ref.compareAndSet(bag,bag,true, false)){}
            System.out.println(bag.toString());
        }).start();

        Thread.sleep(1000);
        System.out.println("主线程想换一只新垃圾袋？");
        boolean success = ref.compareAndSet(prev, new GarbageBag("空垃圾袋"), true, false);
        System.out.println("换了吗？"+success);

        System.out.println(ref.getReference().toString());
    }
}


class GarbageBag{
    String desc;

    public GarbageBag(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "GarbageBag{" +
                "desc='" + desc + '\'' +
                '}';
    }
}