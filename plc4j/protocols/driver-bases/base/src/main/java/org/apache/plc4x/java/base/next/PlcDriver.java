package org.apache.plc4x.java.base.next;

import java.util.concurrent.Future;

public interface PlcDriver {

    boolean validate(String fieldQuery);

    Future<PlcConnection> connect();

}
