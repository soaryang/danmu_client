/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package cn.partytime.netty.client;

import cn.partytime.netty.client.handler.ServerWebSocketClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;


@Component
public final class ServerWebSocketClient {


    @Autowired
    private ServerWebSocketClientInitializer serverWebSocketClientInitializer;

    private EventLoopGroup loop = new NioEventLoopGroup();

    static final String URL = System.getProperty("url", "ws://192.168.1.147:8080/ws");


    public  void initBootstrap() throws URISyntaxException {
        URI uri = new URI(URL);
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            final int port = uri.getPort();
            bootstrap.group(loop);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(serverWebSocketClientInitializer);
            ChannelFuture channelFuture = bootstrap.connect(uri.getHost(), port).sync();
            channelFuture.channel().closeFuture().sync();

            //bootstrap.connect().addListener(new ConnectionListener(this));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.print("clinet==========reconect");
            group.shutdownGracefully();
            initBootstrap();

        }
    }
}
