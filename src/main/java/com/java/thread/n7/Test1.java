package com.java.thread.n7;


import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * @Author ljw
 * @Date 2020/12/4 16:45
 * @Version 1.0
 * desc:
 */
public class Test1 {

    public static void main(String[] args) {
        DateTimeFormatter stf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(int i = 0; i<10; i++){
            new Thread(()->{
                TemporalAccessor parse = stf.parse("2020-09-26");
                System.out.println(parse);
            }).start();
        }
    }
}
