package org.apache.plc4x.java.base.next.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.ReferenceCountUtil;
import org.apache.plc4x.java.base.next.PlcConnection;
import org.apache.plc4x.java.base.next.PlcConnectionImpl;
import org.apache.plc4x.java.base.next.PlcProtocol;
import org.apache.plc4x.java.base.next.commands.Message;
import org.apache.plc4x.java.base.next.commands.ReadRequest;
import org.apache.plc4x.java.base.next.commands.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class is the binding element between a {@link org.apache.plc4x.java.base.next.PlcProtocol},
 * netty and the High Level API classes like @{@link org.apache.plc4x.java.base.next.PlcConnection}.
 */
public class PlcApiCodec extends ByteToMessageCodec<Message> {

    private static final Logger logger = LoggerFactory.getLogger(PlcApiCodec.class);

    private final PlcProtocol protocol;

    // We have to keep track of all "in flight" messages here
    private PlcConnection plcConnection;

    // TODO perhaps factory or class name?
    // TODO Perhaps connection layer here?
    public PlcApiCodec(PlcProtocol protocol) {
        this.protocol = protocol;
    }

    @Override public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.protocol.init();
    }

    @Override public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        this.protocol.close();
    }

    @Override public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // ?
    }

    @Override public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // ?
    }

    @Override protected void encode(ChannelHandlerContext ctx, Message command, ByteBuf out) throws Exception {
        // Check exact message type and forward
        if (command instanceof ReadRequest) {
            logger.debug("Received read request {}", command);
            final ByteBuf buffer = Unpooled.buffer();
            protocol.encode(((ReadRequest) command), buffer);
            ctx.writeAndFlush(buffer);
        } else {
            throw new IllegalStateException("The Command " + command.getClass() + " is not implemented!");
        }
    }

    @Override protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        logger.debug("Received message\n"
            + ByteBufUtil.prettyHexDump(in));
        in.markReaderIndex();
        try {
            Response response = protocol.decode(in);
            this.plcConnection.handleResponse(response);
            ReferenceCountUtil.release(in);
        } catch (PlcProtocol.UnableToParseException e) {
            in.resetReaderIndex();
        }
    }

    @Override public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // General Exception handling, notify Connection
        this.plcConnection.fireException(cause);
    }

    public void registerConnection(PlcConnectionImpl plcConnection) {
        this.plcConnection = plcConnection;
    }
}
