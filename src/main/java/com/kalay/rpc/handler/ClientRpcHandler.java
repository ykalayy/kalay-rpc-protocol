package com.kalay.rpc.handler;

import com.kalay.rpc.client.ResponseHandler;
import com.kalay.rpc.transport.message.v1.KalayRpcCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static com.kalay.rpc.common.Common.COLON;
import static com.kalay.rpc.common.ProtoCommon.*;

public class ClientRpcHandler extends ChannelInboundHandlerAdapter {

    private final String planTextCommand;
    private final ResponseHandler responseHandler;

    public ClientRpcHandler(KalayRpcCommand kalayRpcCommand, ResponseHandler responseHandler) {
        this.planTextCommand = this.convertCommand(kalayRpcCommand);
        this.responseHandler = responseHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        // Send rpc request to server
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeBytes(this.planTextCommand.getBytes());
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf methodResponse = (ByteBuf) msg;
        String methodResponseAsString = convertBuf(methodResponse);
        try {
            this.responseHandler.handleResponse(methodResponseAsString);
        } finally {
            methodResponse.release();
            ctx.close();
        }
    }

    private String convertBuf(ByteBuf methodResponse) {
        StringBuilder sb = new StringBuilder();
        while (methodResponse.isReadable()) {
            byte b = methodResponse.readByte();
            sb.append((char) b);
        }
        return sb.toString();
    }

    /**
     * REQUEST
     * KALAY 0.0.1
     * Method:{{RemoteFunction}}
     */
    private String convertCommand(KalayRpcCommand kalayRpcCommand) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(kalayRpcCommand.getType().name());
        sb.append("\n");
        sb.append(KALAY_PROTOC_HEADER);
        sb.append(" ");
        sb.append(kalayRpcCommand.getVersion());
        sb.append("\n");
        sb.append(METHOD);
        sb.append(COLON);
        sb.append(kalayRpcCommand.getMethod());

        return sb.toString();
    }
}
