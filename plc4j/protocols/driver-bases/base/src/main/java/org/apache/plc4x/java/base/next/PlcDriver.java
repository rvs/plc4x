package org.apache.plc4x.java.base.next;

import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.base.next.netty.TcpTransportFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Future;

/**
 * Question: Is this a shared instance (Singleton) or one for each "connection",
 * then we need a factory.
 *
 * s7://10.100.110.242&rackId=4
 * s7-serial://10.100.110.242
 */
public interface PlcDriver {

    boolean validate(String fieldQuery);

    Future<PlcConnection> connect(String connection) throws PlcConnectionException;

    boolean accepts(String connection);

    PlcTransportFactory getTransport();

    interface ConnectionParserFactory {

        ConnectionParser parse(String connection) throws PlcConnectionException;

    }

    interface ConnectionParser {

        // PlcTransportFactory getTransport();

        SocketAddress getSocketAddress();

    }
}
