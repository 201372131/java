package com.java.thread.gongxiangmoxing;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author ljw
 * @Date 2020/12/4 8:41
 * @Version 1.0
 * desc:
 */
public class TestAccount {

    public static void main(String[] args) {
        Account account = new AccountCas(10000);
        Account.demo(account);
    }
}

//不加锁实现线程安全
class AccountCas implements Account{
    private AtomicInteger balance;

    public AccountCas(int balance) {
        this.balance = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
        /*while (true){
            //获取余额的最新值
            int prev = balance.get();
            //要修改的余额
            int next = prev - amount;
            //真正修改----比较并设置----在CPU层面保证线程安全  compareAndSet内部是原子的
            if(balance.compareAndSet(prev, next)){
                break;
            }
        }*/
        //简洁写法
        balance.getAndAdd(-1 * amount);

    }
}

//加锁实现线程安全
class AccountUnsafe implements Account{

    private Integer balance;
    @Override
    public Integer getBalance() {
        synchronized (this){
            return this.balance;

        }
    }

    @Override
    public void withdraw(Integer amount) {
        synchronized (this){//通过加锁保护临界资源被多线程访问时的安全
            this.balance -= amount;
        }
    }

    public AccountUnsafe(Integer balance) {
        this.balance = balance;
    }
}

interface Account{
    Integer getBalance();

    void withdraw(Integer amount);

    static void demo(Account account){
        ArrayList<Thread> ts = new ArrayList<>();
        for(int i = 0; i<1000; i++){
            ts.add(new Thread(()->{
                account.withdraw(10);
            }));
        }
        long start = System.nanoTime();
        ts.forEach(Thread::start);
        ts.forEach(t ->{
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();

        System.out.println(account.getBalance()+":cost:"+(end-start)/1000_000+" ms");

    }
}
