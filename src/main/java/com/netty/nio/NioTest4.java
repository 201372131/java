package com.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author ljw
 * @Date 2020/11/13 10:37
 * @Version 1.0
 * desc:从一个文件中获取内容放到另外一个文件中,Nio的方式
 *
 * 1，从FileInputStream获取到FileChannel对象
 * 2，创建Buffer
 * 3，将数据从Channel读取到Buffer中
 *
 * 绝对方法与相对方法
 * 相对方法：limit值与position值会在操作是被考虑到
 * 绝对方法：完全忽略掉limit值与position值
 *
 */
public class NioTest4 {

    public static void main(String[] args) throws Exception{
        FileInputStream inputStream = new FileInputStream("input.txt");
        FileOutputStream outputStream = new FileOutputStream("output.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);
        while (true){
            /**
             * clear会将position重置为0
             */
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
