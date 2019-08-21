package org.apache.plc4x.java.base.next;

import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.base.next.netty.TcpTransportFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Future;

/**
 * Question: Is this a shared instance (Singleton) or one for each "connection",
 * then we need a factory.
 */
public interface PlcDriver {

    boolean validate(String fieldQuery);

    Future<PlcConnection> connect(String connection) throws PlcConnectionException;

    boolean accepts(String connection);

    interface ConnectionParserFactory {

        ConnectionParser parse(String connection) throws PlcConnectionException;

    }

    interface ConnectionParser {

        PlcTransportFactory getTransport();

        SocketAddress getSocketAddress();

    }
}
