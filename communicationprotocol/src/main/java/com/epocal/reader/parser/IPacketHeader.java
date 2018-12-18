package com.epocal.reader.parser;

import com.epocal.reader.common.EpocVersion;

/**
 * Created by dning on 6/13/2017.
 */

public interface IPacketHeader {
    byte[] getPreamble();
    void setPreamble(byte[] preamble);

    byte getProtoGeneration();
    void setProtoGeneration(byte protoGeneration);

    byte getHeaderLength();
    void setHeaderLength(byte headerLength);

    byte getInterfaceType();

    void setInterfaceType(byte interfaceType);

    EpocVersion getInterfaceVersion();
    void setInterfaceVersion(EpocVersion interfaceVersion);

    byte getSourceDevice();
    void setSourceDevice(byte sourceDevice);

    byte getDestinationDevice();
    void setDestinationDevice(byte destinationDevice);

    short getPacketNumber();
    void setPacketNumber(short packetNumber);

    short getPayloadLength();
    void setPayloadLength(short payloadLength);

    short getCRC();
    void setCRC(short CRC);

    byte[] createHeader();
}
