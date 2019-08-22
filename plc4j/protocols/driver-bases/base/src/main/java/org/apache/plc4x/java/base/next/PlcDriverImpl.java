package org.apache.plc4x.java.base.next;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Base class for the Drivers.
 * All that a new Protocol has to override is the Parsing for the Fields (TODO general)
 * and a specific instance of a Protocol.
 */
public abstract class PlcDriverImpl implements PlcDriver {

    protected abstract ConnectionParserFactory parserFactory();

    protected abstract PlcProtocol getProtocol();

    @Override public Future<PlcConnection> connect(String connection) throws PlcConnectionException {
        // Parse connection
        final ConnectionParser parser = parserFactory().parse(connection);
        // Prepare the Completable Future
        final CompletableFuture<PlcConnection> returnFuture = new CompletableFuture<>();
        // Establish the connection with callback for Async
        final PlcTransportFactory transportFactory = getTransport();
        final ChannelFuture channelFuture = transportFactory.connect(getProtocol(), parser.getSocketAddress());
        channelFuture.addListener(new GenericFutureListener<io.netty.util.concurrent.Future<? super Void>>() {
            @Override public void operationComplete(io.netty.util.concurrent.Future<? super Void> f) throws Exception {
                // If successful return the new Connection instance, if not, cancel
                if (f.isSuccess()) {
                    returnFuture.complete(new PlcConnectionImpl(PlcDriverImpl.this, transportFactory.getCodec(), ((ChannelFuture) f).channel()));
                } else {
                    returnFuture.completeExceptionally(f.cause());
                }
            }
        });
        return returnFuture;
    }

}
