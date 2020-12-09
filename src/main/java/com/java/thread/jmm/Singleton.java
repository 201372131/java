package com.java.thread.jmm;

import java.io.Serializable;

/**
 * @Author ljw
 * @Date 2020/12/3 18:57
 * @Version 1.0
 * desc:
 */
//加final防止子类覆盖父类破坏单例
public final class Singleton implements Serializable {
    //私有的构造方法不能防止反射创建新的实例
    private Singleton(){};
    //静态成员变量在类加载时运行多线程下可以保证单例(JVM保证线程安全)
    private static final Singleton INSTANCE = new Singleton();

    public static Singleton getInstance(){
        return INSTANCE;
    }

    public Object readResovle(){//防止反序列化生成另一个对象
        return INSTANCE;
    }
}


