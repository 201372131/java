package com.netty;

/**
 * @Author ljw
 * @Date 2020/11/9 11:45
 * @Version 1.0
 */
public class Netty {

    /**
     * Channel:
     * channel是JavaNIO的一个基本构造。他代表一个实体的开放连接，如读写操作
     * Channel --  Socket
     */

    /**
     * EventLoop -- 控制流，多线程处理，并发
     *
     */

    /**
     * 回调：
     *
     */

    /**
     * Future:
     * future提供了另一种在操作完成时通过应用程序的方式，这个对象可以看作是异步操作结果的占位符，他将在未来的某个时刻完成并对其结果的访问
     *
     * netty提供了它自己的实现ChannelFuture,用于在执行异步操作的时候使用
     * ChannelFuture提供了集中额外的方法，使得我们能够注册一个或者多个ChannelFutureListener实例，监听器的回调方法operationComplete(),将会在
     * 对应的操作完成时被调用，然后监听器可以判断该操作是成功地完成了还是出错了
     *
     * ChannelFuture -- 异步通知
     */

    /**
     * ChannelHandler:
     * 每个ChannelHandler的实例都类似于一种为了响应特定事件而被执行的回调
     */
}
