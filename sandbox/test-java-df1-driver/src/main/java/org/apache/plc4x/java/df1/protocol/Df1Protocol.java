/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */
package org.apache.plc4x.java.df1.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import org.apache.plc4x.java.api.exceptions.PlcProtocolException;
import org.apache.plc4x.java.base.PlcByteToMessageCodec;
import org.apache.plc4x.java.df1.DF1Command;
import org.apache.plc4x.java.df1.DF1Symbol;
import org.apache.plc4x.java.df1.DF1SymbolMessageFrame;
import org.apache.plc4x.java.df1.DF1UnprotectedReadRequest;
import org.apache.plc4x.java.df1.io.DF1SymbolIO;
import org.apache.plc4x.java.utils.ReadBuffer;
import org.apache.plc4x.java.utils.WriteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Df1Protocol extends PlcByteToMessageCodec<DF1Command> {

    private static final Logger logger = LoggerFactory.getLogger(Df1Protocol.class);

    private final short localAddr;
    private final short remoteAddr;
    private final DF1SymbolIO df1SymbolIO;

    public Df1Protocol(short localAddr, short remoteAddr) {
        this.localAddr = localAddr;
        this.remoteAddr = remoteAddr;
        df1SymbolIO = new DF1SymbolIO();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, DF1Command msg, ByteBuf out) throws Exception {
        // Create a new df1 frame for transmitting the command
        DF1SymbolMessageFrame frame = new DF1SymbolMessageFrame(remoteAddr, localAddr, msg);

        // Serialize the message
        WriteBuffer writeBuffer = new WriteBuffer(frame.getLengthInBytes());
        df1SymbolIO.serialize(writeBuffer, frame);
        byte[] data = writeBuffer.getData();

        // Send the serialized data
        out.writeBytes(data);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        short size = 0x00;

        // Yes, it's a little complicated, but we need to find out if we've got enough data.
        if(in.readableBytes() > 2) {
            if(in.getUnsignedByte(0) != (short) 0x10) {
                logger.warn("Expecting DF1 magic number: {}", 0x10);
                if (logger.isDebugEnabled()) {
                    logger.debug("Got Data: {}", ByteBufUtil.hexDump(in));
                }
                exceptionCaught(ctx, new PlcProtocolException(
                    String.format("Expecting DF1 magic number: %02X", 0x10)));
                return;
            }

            short symbolType = in.getUnsignedByte(1);
            switch (symbolType) {
                case (short) 0x02: {
                    if(in.readableBytes() < 5) {
                        return;
                    }
                    short commandType = in.getUnsignedByte(4);
                    switch (commandType) {
                        case (short) 0x01: {
                            if(in.readableBytes() < 11) {
                                return;
                            }
                            break;
                        }
                        case (short) 0x41: {
                            /*int transactionCounter = in.getUnsignedShort(6);
                            if(!readRequestSizes.containsKey(transactionCounter)) {
                                logger.warn("Unknown transaction counter: {}", transactionCounter);
                                if (logger.isDebugEnabled()) {
                                    logger.debug("Got Data: {}", ByteBufUtil.hexDump(in));
                                }
                                exceptionCaught(ctx, new PlcProtocolException(
                                    String.format("Unknown transaction counte: %04X", transactionCounter)));
                                return;
                            }
                            size = readRequestSizes.remove(transactionCounter);
                            if(in.readableBytes() < 8 + size) {
                                return;
                            }*/
                            // TODO: Let's just assume all is good for now ...
                            break;
                        }
                    }
                    break;
                }
                case (short) 0x03: {
                    if(in.readableBytes() < 4) {
                        return;
                    }
                    break;
                }
            }
        }

        // Parse the message received from the DF1 device
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        ReadBuffer readBuffer = new ReadBuffer(data);
        DF1Symbol resp = df1SymbolIO.parse(readBuffer, size);

        // Add the received message to the output
        out.add(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        //super.exceptionCaught(ctx, cause);
    }
}
