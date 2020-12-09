package com.java.thread;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *@Author ljw
 *@Date ${DATE} ${TIME}
 *@Version 1.0
 *desc:保护性暂停
 */
public class Test20 {

    public static void main(String[] args) throws InterruptedException {
        /*GuardedObject guardedObject = new GuardedObject();
        new Thread(()->{
            //等待结果
            System.out.println("等待结果中");
            Object object = guardedObject.get(2000);
            System.out.println("结果:"+object);
        },"t1").start();

        new Thread(()->{
            System.out.println("开始");
            try {
                Thread.sleep(1000);
                guardedObject.complete(new Object());
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"t3").start();

        new Thread(()->{
            System.out.println("执行下载");
            try {
                List<String> list = Downloader.download();
                guardedObject.complete(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        },"t2").start();*/

        for(int i = 0; i<3; i++){
            new people().start();
        }
        Thread.sleep(1000);
        for(Integer id : Mailboxes.getIds()){
            new Postman(id,"内容"+id).start();
        }


    }

}

class people extends Thread{
    @Override
    public void run() {
        //收信
        GuardedObject guardedObject = Mailboxes.createGuardedObject();
        System.out.println("people收信："+guardedObject.getId());
        Object mail = guardedObject.get(5000);
        System.out.println("people收到信："+guardedObject.getId()+"内容："+mail);
    }
}

class Postman extends Thread{
    private int id;
    private String mail;

    public Postman(int id, String mail){
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject guardedObject = Mailboxes.getGuardedObject(id);
        System.out.println("Postman送信id："+id +"内容："+mail);
        guardedObject.complete(mail);
    }
}

class Mailboxes{
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();

    private static int id = 1;
    //产生唯一id
    private static synchronized int generateId(){
        return id++;
    }

    public static GuardedObject getGuardedObject(int id){
        return boxes.remove(id);//返回并移除对象
    }

    public static GuardedObject createGuardedObject(){
        GuardedObject go = new GuardedObject(generateId());
        boxes.put(go.getId(),go);
        return go;
    }

    public static Set<Integer> getIds(){
        return boxes.keySet();
    }
}

class GuardedObject{

    //结果
    private Object response;

    //唯一guardedObject标识
    private int id;

    public GuardedObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    //获取结果
    public Object get(long timeout){//等待timeout时间后还没结果就不在等待了
        synchronized (this){
            //开始时间
            long begin = System.currentTimeMillis();
            //等待时间
            long passedTime = 0;
            //没有结果
            while (response == null){
                long waitTime = timeout - passedTime;
                if(waitTime <= 0){//等待时间超时
                    break;
                }
                try {
                    this.wait(waitTime);//避免虚假唤醒等待超时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    //产生结果
    public void complete(Object response){
        synchronized (this){
            //给结果成员变量赋值
            this.response = response;
            this.notifyAll();//持有this对象锁的都唤醒
        }
    }
}


class Downloader{
    public static List<String> download() throws IOException{
        HttpURLConnection conn = (HttpURLConnection) new URL("https://www.baidu.com/").openConnection();
        List<String> lines = new ArrayList<>();
        try(
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))){
                String line;
                while((line = reader.readLine()) != null){
                    lines.add(line);
            }
        }
        return lines;
    }
}
