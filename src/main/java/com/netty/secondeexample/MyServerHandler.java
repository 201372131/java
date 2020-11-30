package com.netty.secondeexample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * @Author ljw
 * @Date 2020/11/11 11:35
 * @Version 1.0
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String> {

    /**
    * @Description: 如果有请求消息，该方法就会被调用
    * @Param: [ctx：上下文，可以获取远程地址等, msg:接收的请求的对象]
    * @Author: ljw
    * @Date: 2020/11/11
    * @Version: 1.0        
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+", "+msg);
        ctx.channel().writeAndFlush("from server:"+ UUID.randomUUID());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
}
