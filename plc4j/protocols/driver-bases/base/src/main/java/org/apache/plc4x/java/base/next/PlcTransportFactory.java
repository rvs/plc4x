package org.apache.plc4x.java.base.next;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.base.next.netty.PlcApiCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * This represents a Transport layer which is used to generate a Netty Socket.
 */
public abstract class PlcTransportFactory {

    private static final Logger logger = LoggerFactory.getLogger(PlcTransportFactory.class);

    private PlcApiCodec codec;

    /** Its necessary to have the codec to add a Callback as Handler **/
    public PlcApiCodec getCodec() {
        return codec;
    }

    /**
     *
     * This Method creates a new Channel and is only used internally by
     * {@link org.apache.plc4x.java.base.next.PlcDriver}s.
     *
     * @param protocol Plc Protocol to use on this Connection
     * @param address  Socket Address to connect to
     * @throws PlcConnectionException If no connection can be established
     *
     * <b>Important</b> This method is responsible for closing the {@link EventLoopGroup}s
     * it creates!
     */
    ChannelFuture connect(PlcProtocol protocol, SocketAddress address) throws PlcConnectionException {
        logger.debug("Trying to establish connection to {}", address);
        try {
            Bootstrap bootstrap = new Bootstrap();
            final EventLoopGroup workerGroup = createEventLoopGroup();
            bootstrap.group(workerGroup);
            bootstrap.channel(getSocketClass());
            // TODO we should use an explicit (configurable?) timeout here
            // Do configuration
            configure(bootstrap);

            // Instanciate codec
            this.codec = new PlcApiCodec(protocol);

            bootstrap.handler(new ChannelInitializer() {
                @Override protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(codec);
                }
            });
            // Start the client.
            final ChannelFuture f = bootstrap.connect(address);
            f.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override public void operationComplete(Future<? super Void> future) throws Exception {
                    if (!future.isSuccess()) {
                        logger.info("Unable to connect, shutting down worker thread.");
                        workerGroup.shutdownGracefully();
                    }
                }
            });
            return f;
        } catch (Exception e) {
            throw new PlcConnectionException("Error creating channel.", e);
        }
    }

    /** Use NiO by default **/
    protected EventLoopGroup createEventLoopGroup() {
        return new NioEventLoopGroup();
    }

    protected abstract Class<? extends Channel> getSocketClass();

    /** Can configure the bootstrap, if necessary **/
    protected abstract void configure(Bootstrap bootstrap);

}
