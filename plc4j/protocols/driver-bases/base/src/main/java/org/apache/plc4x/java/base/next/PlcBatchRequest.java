package org.apache.plc4x.java.base.next;

import java.util.concurrent.Future;

public interface PlcBatchRequest {

    PlcBatchRequest read(String alias, String fieldQuery);

    <T> PlcBatchRequest write(String alias, String fieldQuery, T value);

    Future<PlcTransactionResponse> execute();

}
