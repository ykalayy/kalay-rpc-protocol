package com.kalay.rpc.transport;

import com.kalay.rpc.transport.message.v1.KalayRpcCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RpcMessageDecoderTest {

    private EmbeddedChannel ch;

    @Before
    public void setupTest() {
        ch = new EmbeddedChannel(new RpcMessageDecoder());
    }


    /**
     * Request
     * KALAY 0.0.1
     * Method:{{RemoteFunction}}
     */
    @Test
    public void simpleCommandParse() throws InterruptedException {
        // Given
        List<String> inputLines = new ArrayList<>();
        inputLines.add("REQUEST");
        inputLines.add("KALAY 0.0.1");
        inputLines.add("Method:{{RemoteFunction}}");
        ByteBuf inputByteBuf = createInput(inputLines);

        // When
        ch.pipeline().fireChannelRead(inputByteBuf);

        KalayRpcCommand command = ch.readInbound();

        // Then
        assertEquals("Type should be same", "REQUEST", command.getType().name());
        assertEquals("Version should be same", "0.0.1", command.getVersion());
        assertEquals("Method name should be RemoteFunction", "{{RemoteFunction}}", command.getMethod());

    }

    private ByteBuf createInput(List<String> inputLines) {
        ByteBuf buf = Unpooled.buffer();

        Iterator<String> it = inputLines.iterator();
        while (it.hasNext()) {
            buf.writeBytes(it.next().getBytes());
            if (it.hasNext()) {
                buf.writeByte(10);
            }
        }
        return buf;
    }

}
