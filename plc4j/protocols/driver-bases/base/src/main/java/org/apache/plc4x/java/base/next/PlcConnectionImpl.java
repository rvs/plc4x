package org.apache.plc4x.java.base.next;

import io.netty.channel.Channel;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.plc4x.java.base.next.commands.FailedRequest;
import org.apache.plc4x.java.base.next.commands.ReadRequest;
import org.apache.plc4x.java.base.next.commands.Response;
import org.apache.plc4x.java.base.next.netty.PlcApiCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Wrapper around nettys {@link Channel} which represents one Connection to a device.
 * This implementation should be (mostly) generic and drivers do NOT have to subclass it.
 */
public class PlcConnectionImpl implements PlcConnection {

    private static final Logger logger = LoggerFactory.getLogger(PlcConnectionImpl.class);

    private final PlcDriver driver;
    private final PlcApiCodec codec;
    private final Channel channel;

    // We have to keep track of all "in flight" messages here
    private final Map<Integer, RequestInformation> requests;
    private final AtomicInteger transactionCounter = new AtomicInteger(0);

    public PlcConnectionImpl(PlcDriver driver, PlcApiCodec codec, Channel channel) {
        this.driver = driver;
        this.codec = codec;
        this.channel = channel;
        requests = new ConcurrentHashMap<>(); // Better to be thread safe here
        // Register this object with the Codec
        this.codec.registerConnection(this);
    }

    @Override public Future<PlcResponse> read(String fieldQuery) {
        // TODO do we parse here or in the protocol??
        // Prepare the container here
        final int transactionId = transactionCounter.getAndIncrement();
        // final ReadRequest request = new ReadRequest(transactionId, fieldQuery);
        final ReadRequest request = new ReadRequest(transactionId, null);
        // Create Request Information Object
        final RequestInformation information = new RequestInformation(transactionId);
        information.setRequest(request);
        information.setStatus(RequestInformation.Status.PREPARING);
        // Register Request
        requests.put(transactionId, information);

        channel.pipeline().write(request).addListener(future -> {
            if (future.isSuccess()) {
                information.setStatus(RequestInformation.Status.SENT);
            } else {
                // TODO make convencience method here?
                information.setStatus(RequestInformation.Status.FAILED);
                information.setCause(future.cause());
                information.getFuture().completeExceptionally(future.cause());
            }
        });
        return information.getFuture();
    }

    @Override public <T> Future<PlcResponse> write(String fieldQuery, T value) {
        // TODO use common base with #read()
        throw new NotImplementedException("");
    }

    @Override public PlcBatchRequest startBatch(IsolationLevel level) {
        throw new NotImplementedException("");
    }

    @Override public void handleResponse(Response response) {
        // Take the necessary action
        assert requests.containsKey(response.getTransactionId());
        final RequestInformation information = requests.get(response.getTransactionId());
        if (response instanceof FailedRequest) {
            information.getFuture().completeExceptionally(((FailedRequest) response).getCause());
        } else {
            throw new IllegalStateException("This type of response " + response.getClass() + " is not implemented");
        }
    }

    @Override public void fireException(Throwable cause) {
        logger.warn("Catched an exception, cancel all pending requests", cause);
        // Find all active requests (future not DONE), and cancel them
        requests.values().stream()
            .map(RequestInformation::getFuture)
            .filter(f -> !f.isDone())
            .forEach(f -> f.completeExceptionally(cause));
    }
}
