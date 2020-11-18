package com.netty.nio;

import java.nio.ByteBuffer;

/**
 * @Author ljw
 * @Date 2020/11/13 11:19
 * @Version 1.0
 * 类型化的put和get
 */
public class NioTest5 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.putInt(15);
        buffer.putLong(50000000L);
        buffer.putDouble(14.234234);
        buffer.putChar('你');
        buffer.putShort((short)2);
        buffer.putChar('我');

        buffer.flip();
        //放置什么数据，读取什么数据
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getDouble());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getChar());

    }
}
