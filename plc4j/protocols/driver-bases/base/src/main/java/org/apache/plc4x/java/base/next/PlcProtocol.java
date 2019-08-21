package org.apache.plc4x.java.base.next;

import io.netty.buffer.ByteBuf;
import org.apache.plc4x.java.base.next.commands.ReadRequest;
import org.apache.plc4x.java.base.next.commands.Response;

public interface PlcProtocol {

    void init();

    void close();

    /**
     * This method encodes a read request to bytes
     */
    void encode(ReadRequest command, ByteBuf out);

    /**
     * This method gets offered a buffer
     * @throws UnableToParseException whenever there are not enough bytes to decode a response
     */
    Response decode(ByteBuf buf) throws UnableToParseException;

    class UnableToParseException extends Exception {

    }
}
