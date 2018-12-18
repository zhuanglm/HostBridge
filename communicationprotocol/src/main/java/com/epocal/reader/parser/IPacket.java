package com.epocal.reader.parser;

import com.epocal.reader.IMsgPayload;

/**
 * Created by dning on 6/13/2017.
 */

public interface IPacket {
    byte[] createPayload(IPacketHeader header, IMsgPayload payload);
}
