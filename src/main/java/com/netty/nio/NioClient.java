package com.netty.nio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author ljw
 * @Date 2020/11/18 10:52
 * @Version 1.0
 * 客户端向服务器端发送数据
 */
public class NioClient {

    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress("127.0.0.1",8899));

            while (true){
                selector.select();
                Set<SelectionKey> keySet = selector.selectedKeys();
                for (SelectionKey selectionKey : keySet){
                    if(selectionKey.isConnectable()){
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        if(client.isConnectionPending()){//连接是否正在进行
                            client.finishConnect();//连接真正建立好了

                            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                            writeBuffer.put((LocalDateTime.now()+" 连接成功").getBytes());
                            writeBuffer.flip();
                            client.write(writeBuffer);

                            ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
                            executorService.submit(() ->{
                                while(true){
                                    try {
                                        writeBuffer.clear();
                                        InputStreamReader input = new InputStreamReader(System.in);
                                        BufferedReader br = new BufferedReader(input);

                                        String sendMessage = br.readLine();

                                        writeBuffer.put(sendMessage.getBytes());
                                        writeBuffer.flip();
                                        client.write(writeBuffer);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        client.register(selector,SelectionKey.OP_READ);
                    }else if(selectionKey.isReadable()){
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        int count = client.read(readBuffer);

                        if(count >0){
                            String receivedMessage = new String(readBuffer.array(), 0, count);
                            System.out.println(receivedMessage);
                        }
                    }
                }
                keySet.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
