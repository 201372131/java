package com.netty.nio;

import java.nio.ByteBuffer;

/**
 * @Author ljw
 * @Date 2020/11/13 11:39
 * @Version 1.0
 * 只读Buffer,我们可以随时将一个普通buffer，调用asReadOnlyBuffer方法返回一个只读Buffer
 * 但不能讲只读buffer转换成读写buffer
 */
public class NioTest7 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println(buffer.getClass());
        for (int i=0; i<buffer.capacity(); ++i){
            buffer.put((byte) i);
        }

        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        readOnlyBuffer.position(0);
        readOnlyBuffer.put((byte) 2);
    }
}
