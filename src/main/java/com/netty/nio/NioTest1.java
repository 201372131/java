package com.netty.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @Author ljw
 * @Date 2020/11/12 14:32
 * @Version 1.0
 * java.io中最核心概念是流（Stream)，面向流的编程。流就是信息载体，通过流获取字节，
 * 流分为输入流和输出流，一个流要么是输入流要么是输出流。不可能即是输入流又是输出流。
 *
 * Java.Nio中拥有三个核心概念：Selector（选择器）, Channel（通道）, Buffer(缓冲区）
 * 面向块（block）或面向缓冲区（Buffer）编程，Buffer本身就是一块内存，底层实现是一个数组，
 * 数据的读写都是通过Buffer实现。及时输出又是输入
 * 我们从Buffer的取数据，而不是Channel中。
 * 除了数组外Buffer还提供了对于数据的结构化访问方式，并且可以追踪到系统的读写过程
 * Java的7中原生类型都有各自对应的Buffer数据类型。如：IntBuffer,LongBuffer,ByteBuffer，没有Boolean等
 *
 * Channel指的是可以向其写入数据或者是从中读取的对象，类似于Java.io的Stream
 * 所有数据的读写都是通过Buffer来进行的，永远不会出现直接向Channel写入数据的情况，或者直接从Channel读取数据的情况。
 *
 * 与Stream不同的是，Channel是双向的，一个流只可能是InputStream或OutputStream. Channel打开后则可以进行读取，写入或者是读写。
 * 由于Channel是双向的，因此它更好地反应出底层操作系统的真是情况，底层操作系统的通道就是双向的。
 *
 * Buffer三个属性
 * position：
 * limit：
 * capacity:
 */
public class NioTest1 {

    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);

        for (int i = 0; i< buffer.capacity(); ++i){
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }
        buffer.flip();//状态翻转，可以实现读写的切换，上面是写下面是读。

        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }


}
