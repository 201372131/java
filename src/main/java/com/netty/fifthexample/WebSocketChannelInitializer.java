package com.netty.fifthexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author ljw
 * @Date 2020/11/12 9:04
 * @Version 1.0
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());//以块的方式写的处理器
        //netty对于请求采用分块或分段的方式，C->S 请求数据1000 ，假设分成10段。该handler就是把这些分块的给聚合到一起，生成一个完整的请求
        pipeline.addLast(new HttpObjectAggregator(8192));//对http消息聚合
        //负责websocket的握手 ，和ping-pong ，以frames传递。
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));//处理websocket繁重的工作
        pipeline.addLast(new TextWebSocketFrameHandler());
    }
}
