package org.apache.plc4x.java.base.next;

import java.util.concurrent.Future;

public interface PlcConnection {

    Future<PlcResponse> execute(PlcRequest request);

    Future<PlcResponse> read(String fieldQuery);

    <T> Future<PlcResponse> write(String fieldQuery, T value);

    PlcTransaction startTransaction(TransactionLevel level);
}
