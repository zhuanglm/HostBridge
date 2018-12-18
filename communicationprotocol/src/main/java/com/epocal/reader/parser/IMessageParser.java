package com.epocal.reader.parser;

import com.epocal.reader.IMsgPayload;
import com.epocal.util.RefWrappers;

/**
 * Created by dning on 6/8/2017.
 */

public interface IMessageParser {
    void addData(byte[] buffer, int startidx, int length);
    IMsgPayload parse(Object userparam);
    int parseMessage(IMsgPayload message, int messageNumber, RefWrappers<byte[]> buffer);
}
