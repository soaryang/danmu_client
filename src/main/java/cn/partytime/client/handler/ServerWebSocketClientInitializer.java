package cn.partytime.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

@Component
public class ServerWebSocketClientInitializer extends ChannelInitializer<SocketChannel> {


    @Autowired
    @Qualifier("serverWebSocketClientHandler")
    private ServerWebSocketClientHandler serverWebSocketClientHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(serverWebSocketClientHandler);
    }
}
