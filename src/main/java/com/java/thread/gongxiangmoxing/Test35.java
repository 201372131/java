package com.java.thread.gongxiangmoxing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author ljw
 * @Date 2020/12/4 10:52
 * @Version 1.0
 * desc:
 */
public class Test35 {
    public static void main(String[] args) {
        DecimalAccount.demo(new DecimalAccountCas(new BigDecimal("10000")));
    }

}

class DecimalAccountCas implements DecimalAccount{

    private AtomicReference<BigDecimal> balance;//线程安全

    public DecimalAccountCas(BigDecimal balance) {
        this.balance = new AtomicReference<>(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        while (true){
            BigDecimal prev = balance.get();
            BigDecimal next = prev.subtract(amount);
            if (balance.compareAndSet(prev, next)) {
                break;
            }
        }
    }
}

interface DecimalAccount{
    BigDecimal getBalance();
    void withdraw(BigDecimal amount);

    static void demo(DecimalAccount account){
        ArrayList<Thread> ts = new ArrayList<>();
        for(int i = 0; i<1000; i++){
            ts.add(new Thread(()->{
                account.withdraw(BigDecimal.TEN);
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
