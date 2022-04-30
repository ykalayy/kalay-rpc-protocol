package com.kalay.rpc.server;

import com.kalay.rpc.RpcService;
import com.kalay.rpc.server.model.ServerRpcMethod;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KalayRpcServer {

    private final Map<String, ServerRpcMethod> methodCache;

    public KalayRpcServer(RpcService serverRpcService) {
        this.methodCache = new ConcurrentHashMap<>();

        this.loadMethods(serverRpcService);
    }

    public void startService(int port) {


        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer(this.methodCache))
                    .bind(port)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    private void loadMethods(RpcService serverRpcService) {
        Method[] methods = serverRpcService.getClass().getMethods();
        for(Method m : methods) {
            m.setAccessible(true);
            this.methodCache.put(m.getName(), new ServerRpcMethod(serverRpcService, m));
        }
    }
}
