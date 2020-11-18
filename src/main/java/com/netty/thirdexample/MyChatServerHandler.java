package com.netty.thirdexample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Author ljw
 * @Date 2020/11/11 14:30
 * @Version 1.0
 */
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    //保存一个一个的channel对象,已经与服务器端建立好连接的客户端的channel对象
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 服务端收到任意一个客户端发过来的一个消息后，就会触发该方法（事件驱动）
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch ->{
            if(channel != ch){
                //当前遍历的channel对象不是发送消息的客户端
                ch.writeAndFlush(channel.remoteAddress() + "发送的消息："+ msg +"\n");
            }else{//自己发送的消息
                ch.writeAndFlush("【自己】"+ msg +"\n");
            }
        } );
    }


    /**
     * 有新的连接加入事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("【服务器】 - "+channel.remoteAddress()+", 加入\n");//广播给其他已经加入的客户端
        channelGroup.add(channel);
    }

    /**
     * 连接断开事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("【服务器】 - "+channel.remoteAddress()+", 离开\n");

        System.out.println("当前剩余连接数："+channelGroup.size());
        //channelGroup.remove(channel);//断掉后netty会自动的寻找channelGroup断掉的channel，所以该行代码netty自动加上，不用自己写
    }

    /**
     * 表示连接处于活动状态事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        System.out.println(channel.remoteAddress()+"上线");
    }

    /**
     * 有下线的事件发生会调用该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"下线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
