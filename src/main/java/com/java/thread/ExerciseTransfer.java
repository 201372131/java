package com.java.thread;

import java.util.Random;

/**
 * @Author ljw
 * @Date 2020/12/1 10:50
 * @Version 1.0
 * desc:转账
 */
public class ExerciseTransfer {

    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000);
        Account b = new Account(1000);
        Thread t1 = new Thread(()->{
            for(int i = 0; i<1000; i++){
                a.transfer(b,randomAmount());
            }
        },"t1");

        Thread t2 = new Thread(()->{
            for(int i = 0; i<1000; i++){
                b.transfer(a,randomAmount());
            }
        },"t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        //查看转账2000次后的总金额
        System.out.println("总金额："+(a.getMoney() + b.getMoney()));

        }
    static Random random = new Random();
    //随机100；
    public static int randomAmount(){
        return random.nextInt(100)+1;
    }

}

class Account{
    private int money;

    public Account(int money){
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money){
        this.money = money;
    }

    //转账
    public void transfer(Account target, int amount){
        synchronized (Account.class){
            if(this.money >= amount){
                this.setMoney(this.getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
            }
        }

    }
}
