package com.java.thread.reentranLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author ljw
 * @Date 2020/12/2 15:44
 * @Version 1.0
 * desc:解决哲学家吃饭死锁问题
 */
public class ReentrantLock3 {

    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philosopher("苏格拉底",c1,c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        new Philosopher("阿基米德", c5, c1).start();
    }

}

class Philosopher extends Thread{
    Chopstick left;
    Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true){
            //尝试获得左手筷子
            if (left.tryLock()) {
                try {
                    //尝试获得右手筷子
                    if (right.tryLock()) {
                        try {
                            eat();
                        }finally {//如果拿right锁失败，在这里释放，不会造成死锁
                            right.unlock();//最后释放锁
                        }
                    }
                }finally {//如果拿left锁失败，在这里释放，不会造成死锁
                    left.unlock();//拿到锁后一定要释放
                }
            }

        }
    }

    private void eat(){
        System.out.println("eating.....");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Chopstick extends ReentrantLock {
    String name;

    public Chopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{"+ name +"}";
    }
}
