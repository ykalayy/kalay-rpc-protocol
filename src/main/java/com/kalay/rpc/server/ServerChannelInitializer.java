package com.kalay.rpc.server;

import com.kalay.rpc.RpcService;
import com.kalay.rpc.handler.ServiceCallerHandler;
import com.kalay.rpc.server.model.ServerRpcMethod;
import com.kalay.rpc.transport.RpcMessageDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.Map;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Map<String, ServerRpcMethod> methodCache;
    private final EventExecutorGroup serverMethodExecutor;

    public ServerChannelInitializer(Map<String, ServerRpcMethod> methodCache) {
        this.methodCache = methodCache;
        this.serverMethodExecutor = new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors() * 2);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("decoder", new RpcMessageDecoder()); // Command decoder
        pipeline.addLast(serverMethodExecutor, "handler", new ServiceCallerHandler(this.methodCache)); // Method executor
    }
}
