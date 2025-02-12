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
package org.apache.plc4x.java.utils.rawsockets.netty;

import org.pcap4j.packet.*;

public class TcpIpPacketHandler implements PacketHandler {

    @Override
    public byte[] getData(Packet packet) {
        EthernetPacket ethernetPacket = (EthernetPacket) packet;
        IpV4Packet ipv4Packet = (IpV4Packet) ethernetPacket.getPayload();
        TcpPacket tcpPacket = (TcpPacket) ipv4Packet.getPayload();
        if(tcpPacket.getPayload() instanceof UnknownPacket) {
            UnknownPacket unknownPacket = (UnknownPacket) tcpPacket.getPayload();
            return tcpPacket.getPayload().getRawData();
        }
        return new byte[0];
    }

}
