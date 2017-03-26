package cn.partytime.netty.client.listener;

import cn.partytime.netty.client.ServerWebSocketClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/3/24 0024.
 */
public class ConnectionListener implements ChannelFutureListener {

    @Resource(name = "clientBootstrap")
    private Bootstrap bootstrap;


    private ServerWebSocketClient client;

    public ConnectionListener(ServerWebSocketClient client) {

        this.client = client;

    }

    @Override

    public void operationComplete(ChannelFuture channelFuture) throws Exception {

        if (!channelFuture.isSuccess()) {

            System.out.println("Reconnect");

            final EventLoop loop = channelFuture.channel().eventLoop();

            loop.schedule(new Runnable() {

                @Override

                public void run() {

                    try {
                        client.initBootstrap();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                }

            }, 1L, TimeUnit.SECONDS);

        }

    }

}
