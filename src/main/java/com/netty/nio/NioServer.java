package com.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @Author ljw
 * @Date 2020/11/17 10:42
 * @Version 1.0
 * 聊天程序服务器端
 * 每一行都要知道原理，意思
 */
public class NioServer {

    private static Map<String,SocketChannel> clientMap = new HashMap<>();//所有客户端连接信息

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);//非阻塞
        ServerSocket serverSocket = serverSocketChannel.socket();
        //端口号的绑定，监听
        serverSocket.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//服务器关注连接事件

        while(true){
            try {
                selector.select();//阻塞方法,返回所关注的事件的数量
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach(selectionKey -> {
                    final SocketChannel client;
                    try{
                        if(selectionKey.isAcceptable()){//客户端向服务端发起了一个连接
                            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                            client = server.accept();
                            client.configureBlocking(false);//阻塞
                            client.register(selector,SelectionKey.OP_READ);

                            String key = "【"+ UUID.randomUUID().toString()+"】";
                            clientMap.put(key,client);
                        }else if(selectionKey.isReadable()){//数据可读了
                            client = (SocketChannel)selectionKey.channel();
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                            int count = client.read(readBuffer);

                            if(count >0){
                                readBuffer.flip();
                                Charset charset = Charset.forName("utf-8");
                                String receivedMessage = String.valueOf(charset.decode(readBuffer).array());
                                System.out.println(client+":"+receivedMessage);

                                String senderKey = null;
                                for(Map.Entry<String, SocketChannel> entry : clientMap.entrySet()){
                                    if(client == entry.getValue()){
                                        senderKey = entry.getKey();
                                        break;
                                    }
                                }

                                for(Map.Entry<String, SocketChannel> entry : clientMap.entrySet()){
                                    SocketChannel value = entry.getValue();
                                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                    writeBuffer.put((senderKey+": "+receivedMessage).getBytes());
                                    writeBuffer.flip();

                                    value.write(writeBuffer);
                                }
                            }


                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });

                selectionKeys.clear();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
