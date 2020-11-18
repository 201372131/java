package com.netty.firstexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author ljw
 * @Date 2020/11/11 9:36
 * @Version 1.0
 */
public class TestServer {
    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup boosGroup = new NioEventLoopGroup();//获取连接  死循环,接收客户端的连接。
        EventLoopGroup workerGroup = new NioEventLoopGroup();//处理连接，响应连接  死循环，
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();//服务器启动类。助手类
            serverBootstrap.group(boosGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)//网络编程通道
                    .childHandler(new TestServerInitializer());//处理器

            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
