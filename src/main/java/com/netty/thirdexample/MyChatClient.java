package com.netty.thirdexample;

import com.netty.secondeexample.MyClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author ljw
 * @Date 2020/11/11 15:31
 * @Version 1.0
 */
public class MyChatClient {

    public static void main(String[] args) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new MyChatClientInitializer());

            Channel channel = bootstrap.connect("localhost", 8899).sync().channel();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            for(; ;){
                channel.writeAndFlush(br.readLine() +"\r\n");
            }
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
