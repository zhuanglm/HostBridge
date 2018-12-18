package com.epocal.reader;

import com.epocal.reader.enumtype.*;
import com.epocal.reader.type.*;

import java.io.IOException;

/**
 * Created by dning on 6/6/2017.
 */

public interface IMsgPayload {
    MessageDescriptor getDescriptor();

    byte getMessageCode();
    void setMessageCode(byte messagecode);

    byte[] getRawBuffer();
    void setRawBuffer(byte[] rawbuffer);

    int fillBuffer();

    ParseResult parseBuffer(byte[] buffer);

    String toString();
}
