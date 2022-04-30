package com.kalay.rpc.transport;

import com.kalay.rpc.transport.message.v1.HeaderParser;
import com.kalay.rpc.transport.message.v1.KalayRpcCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RpcMessageDecoder extends ByteToMessageDecoder {

    private static final Logger log = LoggerFactory.getLogger(RpcMessageDecoder.class.getName());


    // Line feed
    private static final int LF = 10;

    /**
     * REQUEST
     * KALAY 0.0.1
     * Method:{{RemoteFunction}}
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        while (byteBuf.isReadable()) {
            KalayRpcCommand kalayRpcCommand = new KalayRpcCommand();

            // Parse Command-Type
            kalayRpcCommand.setType(
                    HeaderParser.parseCommandType(readLine(byteBuf)));

            // Parse Version Header
            kalayRpcCommand.setVersion(
                    HeaderParser.versionParser(readLine(byteBuf)));

            // Parse and register Method
            kalayRpcCommand.setMethod(
                    HeaderParser.methodParser(readLine(byteBuf)));

            out.add(kalayRpcCommand);
        }
    }

    private String readLine(ByteBuf byteBuf) {
        StringBuilder sb = new StringBuilder();
        byte b;
        while ( byteBuf.isReadable() && (b = byteBuf.readByte()) != LF) {
            sb.append((char) b);
        }
        return sb.toString();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the current channel
        // Incoming packets not align with protocol
        log.error("Incoming request is not align with current protocol. Closing the socket");
        ctx.close();
    }
}
