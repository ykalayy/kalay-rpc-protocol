package com.kalay.rpc.handler;

import com.kalay.rpc.server.model.ServerRpcMethod;
import com.kalay.rpc.transport.message.v1.KalayRpcCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class ServiceCallerHandler extends SimpleChannelInboundHandler<KalayRpcCommand> {

    private static final Logger log = LoggerFactory.getLogger(ServiceCallerHandler.class.getName());
    private final Map<String, ServerRpcMethod> methodCache;

    public ServiceCallerHandler(Map<String, ServerRpcMethod> methodCache) {
        this.methodCache = methodCache;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, KalayRpcCommand kalayRpcCommand) throws Exception {

        String methodName = kalayRpcCommand.getMethod();
        ServerRpcMethod rpcMethod = methodCache.get(kalayRpcCommand.getMethod());
        if (Objects.nonNull(rpcMethod)) {
            Method targetMethod = rpcMethod.getTargetMethod();
            Object serverInstance = rpcMethod.getServerInstance();

            // Execute function
            String planTextResponse = (String) targetMethod.invoke(serverInstance);

            // Write the response to client
            channelHandlerContext.channel().writeAndFlush(
                    convertToByteBuf(channelHandlerContext.alloc().buffer() ,planTextResponse));
        } else {
            // UnSupported method is called...
            log.warn("Incoming method is unsupported. MethodName:{}", methodName);
            channelHandlerContext.close();
        }
    }

    private ByteBuf convertToByteBuf(ByteBuf buffer, String planTextResponse) {
        buffer.writeBytes(planTextResponse.getBytes(StandardCharsets.UTF_8));
        return buffer;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        // Close the channel
        cause.printStackTrace();
        log.error("Unknown exception occurred. While trying to call server method. Ex:{]", cause.getCause());
        ctx.close();
    }
}
