package com.kalay.rpc.client;

import com.kalay.rpc.handler.ClientRpcHandler;
import com.kalay.rpc.transport.message.v1.KalayRpcCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KalayRpcClient {

    private static final Logger log = LoggerFactory.getLogger(KalayRpcClient.class.getName());

    private final String host;
    private final int port;

    public KalayRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void call(KalayRpcCommand kalayRpcCommand, ResponseHandler responseHandler) throws InterruptedException {


        log.debug("Starting the remote call into Host:{} Port: {}", host, port);
        EventLoopGroup worker = new NioEventLoopGroup(2);
        try {
            Bootstrap b = new Bootstrap();
            b.group(worker);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ClientRpcHandler(kalayRpcCommand, responseHandler));
                }
            });
            // Start the client
            ChannelFuture f = b.connect(this.host, this.port).sync();
            f.channel().closeFuture().sync(); // Wait until channel is closed
            log.debug("Remote function is executed");

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } finally {
            worker.shutdownGracefully();

        }

    }
}
