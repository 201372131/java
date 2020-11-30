package com.netty.secondeexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author ljw
 * @Date 2020/11/11 11:25
 * @Version 1.0
 */
public class MyServer {

    /**
     * handler和childHandler区别：
     * handler对boosGroup发挥作用
     * childHandler对workerGroup发挥作用
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        /**
         * EventLoopGroup是一个死循环
         */
        EventLoopGroup boosGroup = new NioEventLoopGroup();//接收客户端的连接转发给workerGroup
        EventLoopGroup workerGroup = new NioEventLoopGroup();//处理用户的请求
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }
}
