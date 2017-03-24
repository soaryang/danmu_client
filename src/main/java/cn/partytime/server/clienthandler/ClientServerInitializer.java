package cn.partytime.server.clienthandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by user on 2016/6/15.
 */

@Component
public class ClientServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    @Qualifier("danmuServerHandler")
    private ClientServerHandler danmuServerHandler;

    @Autowired
    private HttpHandler webSocketServerHandler;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("idleStateHandler", new IdleStateHandler(20, 10, 0));
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(webSocketServerHandler);
        pipeline.addLast(danmuServerHandler);
    }
}