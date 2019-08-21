package org.apache.plc4x.java.base.next.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.plc4x.java.base.next.PlcTransportFactory;

public class TcpTransportFactory extends PlcTransportFactory {

    @Override protected Class<? extends Channel> getSocketClass() {
        return NioSocketChannel.class;
    }

    @Override protected void configure(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
    }

}
