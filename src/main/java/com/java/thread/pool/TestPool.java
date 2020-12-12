package com.java.thread.pool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author ljw
 * @Date 2020/12/7 11:04
 * @Version 1.0
 * desc:手写线程池
 */
public class TestPool {

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(1, 1000, TimeUnit.SECONDS,
                1, (queue, task)->{
            //死等
            //queue.put(task);
            //带超时的等待
            //queue.offer(task, 1500, TimeUnit.MILLISECONDS);
            //让调用者放弃任务执行
            //System.out.println("放弃："+task);
            //抛异常
            //throw new RuntimeException("任务执行失败"+task);
            //让调用者自己执行任务
            task.run();
                });
        for(int i = 0; i < 3; i++){
            int j = i;
            threadPool.execute(()->{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}

@FunctionalInterface//拒绝策略
interface RejectPolicy<T>{
    void reject(BlockingQueue<T> queue, T task);
}


//线程池
class ThreadPool{
    //任务队列
    private BlockingQueue<Runnable> taskQueue;
    //线程集合
    private HashSet<Worker> workers = new HashSet<>();
    //核心线程数
    private int coreSize;
    //获取任务超时时间
    private long timeout;

    private TimeUnit timeUnit;
    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectPolicy = rejectPolicy;
    }

    //执行任务----接收参数为要执行的任务对象
    public void execute(Runnable task){
        //当任务数没有超过coreSize时，直接交给worker对象执行
        //如果任务数超过coreSize时，加入任务队列暂存
        synchronized (workers){
            if(workers.size() < coreSize){
                Worker worker = new Worker(task);
                System.out.println("新增worker:  "+worker+", task:"+task);
                workers.add(worker);
                worker.start();
            }else {
                //死等
                //taskQueue.put(task);

                //带超时等待
                //让调用者放弃任务执行
                //让调用者抛出异常
                //让调用者自己执行任务
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            //执行任务
            //当task不为空，执行任务
            //当task执行完毕，再接着从任务队列获取任务并执行
            while (task != null || (task = taskQueue.take()) != null){
            //while (task != null || (task = taskQueue.poll(timeout, timeUnit)) != null){
                try {
                    System.out.println("正在执行："+task);
                    task.run();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    task = null;
                }
            }
            synchronized (workers){
                System.out.println("worker被移除："+workers);
                workers.remove(this);
            }
        }
    }
}


//阻塞队列
class BlockingQueue<T>{
    //1 任务队列
    private Deque<T> queue = new ArrayDeque<>();
    //2 锁
    private ReentrantLock lock = new ReentrantLock();
    //3 生产者条件变量
    private Condition fullWaitSet = lock.newCondition();
    //4 消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();
    //5 容量
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    //阻塞获取
    public T take(){
        lock.lock();
        try {
            while (queue.isEmpty()){
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        }finally {
            lock.unlock();
        }
    }

    //带超时的阻塞获取
    public T poll(long timeout, TimeUnit unit){
        lock.lock();
        try {
            //将timeout 转换成纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()){
                try {
                    if(nanos <= 0){
                        return null;
                    }
                    //返回的是剩余时间
                    nanos = emptyWaitSet.awaitNanos(nanos);//虚假唤醒问题，如果等待的时候被唤醒了，但是没有抢到又会进行等待timeout时间。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        }finally {
            lock.unlock();
        }
    }


    //阻塞添加
    public void put(T task){
        lock.lock();
        try {
            while (queue.size() == capacity){
                try {
                    System.out.println("等待加入任务队列："+task);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("加入任务队列："+task);
            queue.addLast(task);
            emptyWaitSet.signal();
        }finally {
            lock.unlock();
        }
    }

    //带超时时间的阻塞添加
    public boolean offer(T task, long timeout, TimeUnit timeUnit){
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (queue.size() == capacity){
                try {
                    System.out.println("等待加入任务队列："+task);
                    if(nanos <= 0){
                        return false;
                    }
                    nanos = fullWaitSet.awaitNanos(nanos);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            System.out.println("加入任务队列："+task);
            queue.addLast(task);
            emptyWaitSet.signal();
            return true;
        }finally {
            lock.unlock();
        }

    }


    //获取大小
    public int size(){
        lock.lock();
        try {
            return queue.size();
        }finally {
            lock.unlock();
        }
    }


    public void tryPut(RejectPolicy<T> rejectPolicy, T task){
        lock.lock();
        try{
            //判断队列是否已满
            if(queue.size() == capacity){
                rejectPolicy.reject(this, task);
            }else {//有空闲
                System.out.println("加入任务队列："+task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        }finally {
            lock.unlock();
        }
    }


}













/*
public class TestPool {

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(2, 1000, TimeUnit.SECONDS, 1,
                (queue, task)->{
                    queue.put(task);
                });
        for(int i = 0; i <3; i++){
            int j = i;
            threadPool.execute(()->{
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(j);
            });
        }
    }
}


//线程池
class ThreadPool{
    //任务队列
    private BlockingQueue<Runnable> taskQueue;
    //线程集合
    private HashSet<Worker> workers = new HashSet<>();
    //核心线程数
    private int coreSize;
    //获取任务超时时间
    private long timeout;

    private TimeUnit timeUnit;

    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectPolicy = rejectPolicy;
    }

    //执行任务----接收参数为要执行的任务对象
    public void execute(Runnable task){
        //当任务数没有超过coreSize时，直接交给worker对象执行
        //如果任务数超过coreSize时，加入任务队列暂存
        synchronized (workers){
            if(workers.size() < coreSize){
                Worker worker = new Worker(task);
                System.out.println("新增worker:  "+worker+", task:"+task);
                workers.add(worker);
                worker.start();
            }else {
                taskQueue.put(task);
                //死等
                //带超时等待
                //让调用者放弃任务执行
                //让调用者抛出异常
                //让调用者自己执行任务
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            //执行任务
            //当task不为空，执行任务
            //当task执行完毕，再接着从任务队列获取任务并执行
            //while (task != null || (task = taskQueue.take()) != null){
            while (task != null || (task = taskQueue.poll(timeout, timeUnit)) != null){
                try {
                    System.out.println("正在执行："+task);
                    task.run();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    task = null;
                }
            }
            synchronized (workers){
                System.out.println("worker被移除："+workers);
                workers.remove(this);
            }
        }
    }
}

@FunctionalInterface //拒绝策略
interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}


//阻塞队列
class BlockingQueue<T>{
    //1 任务队列
    private Deque<T> queue = new ArrayDeque<>();
    //2 锁
    private ReentrantLock lock = new ReentrantLock();
    //3 生产者条件变量
    private Condition fullWaitSet = lock.newCondition();
    //4 消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();
    //5 容量
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    //阻塞获取
    public T take(){
        lock.lock();
        try {
            while (queue.isEmpty()){
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        }finally {
            lock.unlock();
        }
    }

    //带超时的阻塞获取
    public T poll(long timeout, TimeUnit unit){
        lock.lock();
        try {
            //将timeout 转换成纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()){
                try {
                    if(nanos <= 0){
                        return null;
                    }
                    //返回的是剩余时间
                    nanos = emptyWaitSet.awaitNanos(nanos);//虚假唤醒问题，如果等待的时候被唤醒了，但是没有抢到又会进行等待timeout时间。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        }finally {
            lock.unlock();
        }
    }


    //阻塞添加
    public void put(T task){
        lock.lock();
        try {
            while (queue.size() == capacity){
                try {
                    System.out.println("等待加入任务队列："+task);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("加入任务队列："+task);
            queue.addLast(task);
            emptyWaitSet.signal();
        }finally {
            lock.unlock();
        }
    }

    //带超时时间的阻塞添加
    public boolean offer(T task, long timeout, TimeUnit timeUnit){
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (queue.size() == capacity){
                try {
                    System.out.println("等待加入任务队列："+ task);
                    if(nanos <= 0){
                        return false;
                    }
                    nanos = fullWaitSet.awaitNanos(nanos);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            System.out.println("加入任务队列");
            queue.addLast(task);
            emptyWaitSet.signal();
            return true;
        }finally {
            lock.unlock();
        }
    }

    //获取大小
    public int size(){
        lock.lock();
        try {
            return queue.size();
        }finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task){
        lock.lock();
        try{
            //判断队列是否已满
            if(queue.size() == capacity){
                rejectPolicy.reject(this, task);
            }else {//有空闲
                System.out.println("加入任务队列："+task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        }finally {
            lock.unlock();
        }
    }

}
*/
