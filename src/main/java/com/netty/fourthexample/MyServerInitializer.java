package com.netty.fourthexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Author ljw
 * @Date 2020/11/11 16:05
 * @Version 1.0
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(5,7,10, TimeUnit.SECONDS));//心跳检测，空闲检测处理器handler，读空闲，写空闲，读写空闲
        pipeline.addLast(new MyServerHandler());
    }
}
