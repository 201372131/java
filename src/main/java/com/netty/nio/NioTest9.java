package com.netty.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author ljw
 * @Date 2020/11/13 14:48
 * @Version 1.0
 *
 * 网络编程
 * ServerSocket serverSocket = ....
 * serverSocket.bind(889);
 * Socket socket = serverSocket.accept();//阻塞方法
 *
 */
public class NioTest9 {

    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("NioTest9", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,0,5);

        mappedByteBuffer.put(0, (byte) 'a');
        mappedByteBuffer.put(3, (byte) 'b');
    }
}
