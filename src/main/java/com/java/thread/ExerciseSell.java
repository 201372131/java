package com.java.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * @Author ljw
 * @Date 2020/12/1 10:10
 * @Version 1.0
 * desc:卖票
 */
public class ExerciseSell {

    public static void main(String[] args) throws InterruptedException {
        TicketWindow ticketWindow = new TicketWindow(2000);
        List<Thread> list = new ArrayList<>();
        // 用来保存卖出去多少张票
        List<Integer> sellCount = new Vector<>();

        for (int i = 0; i < 4000; i++) {
            Thread t = new Thread(() -> {
                // 分析这里的竞态条件
                int count = ticketWindow.sell(randomAmount());
                sellCount.add(count);
            });
            list.add(t);
            t.start();
        }
        for(Thread thread : list){
            thread.join();//等待所有线程结束后再往下执行
        }
        // 买出去的票求和
        System.out.println("卖出去的票数："+sellCount.stream().mapToInt(c -> c).sum());
        // 剩余票数
        System.out.println("剩余票数:"+ticketWindow.getCount());
    }

    // Random 为线程安全
    static Random random = new Random();

    // 随机 1~5
    public static int randomAmount() {
        return random.nextInt(5) + 1;
    }
}



    //售票窗口
    class TicketWindow{
        private int count;

        public TicketWindow(int count){
            this.count = count;
        }

        //获取余票数量
        public int getCount(){
            return count;
        }

        //售票
        public int sell(int amount){
            if(this.count >= amount){
                this.count -= amount;
                return amount;
            }else {
                return 0;
            }
        }
    }

