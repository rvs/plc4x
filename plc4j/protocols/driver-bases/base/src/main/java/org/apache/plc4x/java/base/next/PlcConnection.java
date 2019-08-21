package org.apache.plc4x.java.base.next;

import org.apache.plc4x.java.base.next.commands.Response;

import java.util.concurrent.Future;

public interface PlcConnection {

    Future<PlcResponse> read(String fieldQuery);

    <T> Future<PlcResponse> write(String fieldQuery, T value);

    PlcBatchRequest startBatch(IsolationLevel level);

    /**
     * Is called from downstream to notify the Connection object, that there is a response
     * for one of its requests.
     */
    void respond(Response response);
}
