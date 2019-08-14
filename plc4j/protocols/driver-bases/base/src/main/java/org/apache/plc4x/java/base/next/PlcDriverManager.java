package org.apache.plc4x.java.base.next;

import java.util.concurrent.Future;

public interface PlcDriverManager {

    Future<PlcConnection> connect(String connection);

    PlcDriver getDriver(String connection);

}
