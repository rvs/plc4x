package org.apache.plc4x.java.base.next;

import java.util.Optional;
import java.util.concurrent.Future;

public interface PlcDriverManager {

    Future<PlcConnection> connect(String connection);

    Optional<PlcDriver> getDriver(String connection);

}
