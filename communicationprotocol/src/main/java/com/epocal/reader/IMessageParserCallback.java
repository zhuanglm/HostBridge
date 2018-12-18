package com.epocal.reader;

/**
 * Created by dning on 6/14/2017.
 */

public interface IMessageParserCallback {
    void parsedMessageReceived(IMsgPayload message);
}
