package com.netty.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author ljw
 * @Date 2020/11/13 15:28
 * @Version 1.0
 * selector
 * 服务器端监听5个端口号，每个都可能有客户端发起连接，使用一个线程处理所有连接
 */
public class NioTest12 {

    public static void main(String[] args) throws Exception{
        int[] port = new int[5];
        port[0] = 5000;
        port[1] = 5001;
        port[2] = 5002;
        port[3] = 5003;
        port[4] = 5004;

        Selector selector = Selector.open();

        for (int i =0; i<port.length; ++i){
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(port[i]);
            serverSocket.bind(address);//绑定

            //注册
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口："+port[i]);
        }

        while (true){
            int number = selector.select();
            System.out.println("numbers: "+number);

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys:"+selectionKeys);

            Iterator<SelectionKey> iter = selectionKeys.iterator();

            while (iter.hasNext()){
                SelectionKey selectionKey = iter.next();

                if(selectionKey.isAcceptable()){
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);

                    iter.remove();
                    System.out.println("获得客户端连接："+socketChannel);
                }else if(selectionKey.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    int byteRead = 0;
                    while (true){
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        byteBuffer.clear();
                        int read = socketChannel.read(byteBuffer);

                        if(read <= 0){
                            break;
                        }
                        byteBuffer.flip();

                        socketChannel.write(byteBuffer);
                        byteRead += read;
                    }
                    System.out.println("读取"+byteRead+",来自于："+socketChannel);
                    iter.remove();
                }
            }
        }


    }
}
