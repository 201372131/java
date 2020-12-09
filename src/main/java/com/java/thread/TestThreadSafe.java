package com.java.thread;

import java.util.ArrayList;

/**
 * @Author ljw
 * @Date 2020/12/1 9:21
 * @Version 1.0
 */
public class TestThreadSafe {
    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
        ThreadUnsafe test = new ThreadUnsafe();
        for(int i =0; i < THREAD_NUMBER; i++){
            new Thread(()->{
                test.method1(LOOP_NUMBER);
            },"Thread"+(i+1)).start();
        }
    }

}

class ThreadUnsafe{
    ArrayList<String> list =  new ArrayList<>();
    public void method1(int loopNumber){
        for(int i = 0; i<loopNumber; i++){
            method2();
            method3();
        }
    }

    private void method2(){
        list.add("1");
    }

    private void method3(){
        list.remove(0);
    }



    class ThreadSafe{
        public final void method1(int loopNumber){
            ArrayList<String> list = new ArrayList<>();
            for(int i = 0; i<loopNumber; i++){
                method2(list);
                method3(list);
            }
        }

        private void method2(ArrayList<String> list){list.add("1");}

        private void method3(ArrayList<String> list){list.remove(0);}
    }
}
