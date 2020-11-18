package com.netty.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author ljw
 * @Date 2020/11/12 14:56
 * @Version 1.0
 */
public class NioTest2 {

    //io切换到Nio
    public static void main(String[] args) throws IOException {

        //获取一个输入流
        FileInputStream fileInputStream = new FileInputStream("NioTest2.txt");
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        fileChannel.read(byteBuffer);

        byteBuffer.flip();//操作翻转，由写到读

        while (byteBuffer.remaining()>0){
            byte b = byteBuffer.get();
            System.out.println("Character: "+(char)b);
        }
        fileInputStream.close();
    }
}
