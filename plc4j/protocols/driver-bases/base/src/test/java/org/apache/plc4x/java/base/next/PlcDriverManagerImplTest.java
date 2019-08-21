package org.apache.plc4x.java.base.next;

import io.netty.buffer.ByteBuf;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.base.next.commands.ReadRequest;
import org.apache.plc4x.java.base.next.commands.Response;
import org.apache.plc4x.java.base.next.netty.TcpTransportFactory;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PlcDriverManagerImplTest {

    @Test
    public void connect() throws PlcConnectionException, ExecutionException, InterruptedException {
        final PlcDriverManagerImpl driverManager = new PlcDriverManagerImpl(Collections.singletonList(new TestDriver()));
        final Optional<PlcDriver> driver = driverManager.getDriver("Hallo");
        assert driver.isPresent();
        final Future<PlcConnection> connectFuture = driver.get().connect("Hallo");
        final PlcConnection connection = connectFuture.get();
        final Future<PlcResponse> readFuture = connection.read("asdf");
        final PlcResponse response = readFuture.get();

        System.out.println("Finished...");
    }

    private static class TestDriver extends PlcDriverImpl {

        @Override protected ConnectionParserFactory parserFactory() {
            return new TestParserFactory();
        }

        @Override protected PlcProtocol getProtocol() {
            return new TestProtocol();
        }

        @Override public boolean validate(String fieldQuery) {
            return true;
        }

        @Override public boolean accepts(String connection) {
            return true;
        }

        private static class TestProtocol implements PlcProtocol {
            @Override public void init() {

            }

            @Override public void close() {

            }

            @Override public void encode(ReadRequest command, ByteBuf out) {
                out.writeBytes("Hallo".getBytes());
            }

            @Override public Response decode(ByteBuf buf) throws UnableToParseException {
                throw new UnableToParseException();
            }
        }

        private static class TestParserFactory implements ConnectionParserFactory {
            @Override public ConnectionParser parse(String connection) throws PlcConnectionException {
                return new TestParser();
            }

            private static class TestParser implements ConnectionParser {
                @Override public PlcTransportFactory getTransport() {
                    return new TcpTransportFactory();
                }

                @Override public SocketAddress getSocketAddress() {
                    return new InetSocketAddress("localhost", 1234);
                }
            }
        }
    }
}