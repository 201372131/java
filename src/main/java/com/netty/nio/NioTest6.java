package com.netty.nio;

import javax.sound.midi.Soundbank;
import java.nio.ByteBuffer;

/**
 * @Author ljw
 * @Date 2020/11/13 11:28
 * @Version 1.0
 * 分片Buffer
 * sliceBuffer和原有的Buffer共享相同的底层数组
 */
public class NioTest6 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for(int i = 0; i<buffer.capacity(); ++i){
            buffer.put((byte)i);
        }

        buffer.position(2);
        buffer.limit(6);

        ByteBuffer sliceBuffer = buffer.slice();

        for (int i =0; i<sliceBuffer.capacity(); ++i){
            byte b = sliceBuffer.get(i);
            b *=2;
            sliceBuffer.put(i,b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
