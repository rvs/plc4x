package org.apache.plc4x.java.base.next;

import java.util.concurrent.Future;

public interface PlcTransaction {

    PlcTransaction read(String alias, String fieldQuery);

    <T> PlcTransaction write(String alias, String fieldQuery, T value);

    Future<PlcTransactionResponse> execute();

}
