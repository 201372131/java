package com.java.thread;


/**
 * @Author ljw
 * @Date 2020/11/30 10:27
 * @Version 1.0
 * desc:两阶段终止
 */
public class Test13 {

    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();

        Thread.sleep(3500);
        tpt.stop();
    }
}

class TwoPhaseTermination{
    private Thread monitorThread;

    //启动线程
    public void start(){
        monitorThread = new Thread(() ->{
            while (true){
                Thread current = Thread.currentThread();
                if(current.isInterrupted()){
                    System.out.println("线程被打断料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);//情况一被打断，打断标识还是false，抛异常
                    System.out.println("执行监控记录");//情况二被打断,打断标识设置为true,不会抛异常！！
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    current.interrupt();//在sleep情况下被打断会抛异常到这里，所以置为打断为true。
                }
            }
        });
        monitorThread.start();
    }

    //停止监控线程
    public void stop(){
        monitorThread.interrupt();
    }
}


class TwoPhaseTermination2{//强化版
    private Thread monitorThread;//监控线程
    private volatile boolean stop = false;//停止标记
    private boolean starting = false;//判断是否执行过start方法

    //启动线程
    public void start(){
        synchronized (this){
            if(starting){
                return;
            }
            starting = true;
        }
        monitorThread = new Thread(() ->{
            while (true){
                Thread current = Thread.currentThread();
                if(stop){
                    System.out.println("线程被打断料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);//情况一被打断，打断标识还是false，抛异常
                    System.out.println("执行监控记录");//情况二被打断,打断标识设置为true,不会抛异常！！
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    current.interrupt();//在sleep情况下被打断会抛异常到这里，所以置为打断为true。
                }
            }
        },"monitor");
        monitorThread.start();
    }


    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination2 tpt = new TwoPhaseTermination2();
        tpt.start();

        Thread.sleep(3500);
        tpt.stop();
    }

    //停止监控线程
    public void stop(){
        stop = true;
        monitorThread.interrupt();
    }
}
