package com.java.thread;

import sun.awt.windows.ThemeReader;

import java.util.LinkedList;

/**
 * @Author ljw
 * @Date 2020/12/2 10:26
 * @Version 1.0
 * desc:生产者/消费者模式
 */
public class Test21 {

    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(2);
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(()->{
                queue.put(new Message(id,"值"+id));
            },"生产者"+i).start();
        }

        new Thread(()->{
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = queue.take();
            }
        },"消费者").start();
    }

}

//消息队列类，java线程之间通信
class MessageQueue{
    //消息队列集合
    private LinkedList<Message> list = new LinkedList<>();
    //队列容量
    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    //获取消息
    public Message take() {
        synchronized (list){
            while (list.isEmpty()){//检查队列是否为空
                System.out.println("队列为空，消费者线程等待");
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = list.removeFirst();//从队列头部获取消息
            System.out.println("已消费消息："+message);
            list.notifyAll();//唤醒队列里面等待存入消息的线程
            return message;
        }
    }

    //存入消息
    public void put(Message message) {
        synchronized (list){
            while (list.size() == capacity){//检查队列是否已满
                System.out.println("队列已满，生产者线程等待");
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.addLast(message);//把消息放入队列尾部
            System.out.println("已生产消息："+message);
            list.notifyAll();//唤醒队列里等待消费的线程来消费
        }
    }
}


final class Message{//加final变安全的类，只能手动创建
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}