package com.netty.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author ljw
 * @Date 2020/11/12 15:02
 * @Version 1.0
 */
public class NioTest3 {

    public static void main(String[] args) throws Exception{
        FileOutputStream fileOutputStream = new FileOutputStream("NioTest3.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byte[] message = "hello world welcome, nihao".getBytes();

        for (int i = 0; i<message.length; i++){
            byteBuffer.put(message[i]);
        }
        byteBuffer.flip();

        //通过buffer写数据，channel和buffer关联。
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
