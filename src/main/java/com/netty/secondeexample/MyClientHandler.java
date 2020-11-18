package com.netty.secondeexample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;


/**
 * @Author ljw
 * @Date 2020/11/11 12:16
 * @Version 1.0
 */
public class MyClientHandler extends SimpleChannelInboundHandler<String> {

    //服务器端向客户端发送消息时候就会触发该方法
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("client output: "+msg);
        ctx.writeAndFlush("from client: "+ LocalDateTime.now());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("来自于客户端的问候");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        super.exceptionCaught(ctx,cause);
        ctx.close();
    }
}
