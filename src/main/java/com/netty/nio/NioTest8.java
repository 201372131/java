package com.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author ljw
 * @Date 2020/11/13 13:57
 * @Version 1.0
 * allocateDirect() 和allocate() 区别
 */
public class NioTest8 {

    public static void main(String[] args) throws Exception{
        FileInputStream inputStream = new FileInputStream("input2.txt");
        FileOutputStream outputStream = new FileOutputStream("output2.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(512);
        while (true){
            buffer.clear();//将缓冲初始化到最初状态
            int read = inputChannel.read(buffer);//每次读取的字节数
            System.out.println("read:"+read);
            if( -1 == read)
                break;

            buffer.flip();
            outputChannel.write(buffer);

        }

        inputChannel.close();
        outputChannel.close();

    }
}
