package com.epocal.reader.nextgen.parser;

import com.epocal.reader.IMsgPayload;
import com.epocal.reader.PreliminaryMessage;

/**
 * Created by dning on 6/12/2017.
 */

public class NextgenMessageParser
{
    private NextgenGenericMessageParser mNextgenGenericMessageParser;
    private NextgenTestMessageParser mNextgenTestMessageParser;
    private NextgenProductionMessageParser mNextgenProductionMessageParser;

    public NextgenMessageParser(){
        mNextgenGenericMessageParser = new NextgenGenericMessageParser();
        mNextgenTestMessageParser = new NextgenTestMessageParser();
        mNextgenProductionMessageParser = new NextgenProductionMessageParser();
    }

    public IMsgPayload parse(PreliminaryMessage msg)
    {
        IMsgPayload message = null;
        switch (msg.descriptor().getMsgClass())
        {
            case Generic:
                message = mNextgenGenericMessageParser.parse(msg);
                break;
            case Test:
                message = mNextgenTestMessageParser.parse(msg);
                break;
            case Production:
                message = mNextgenProductionMessageParser.parse(msg);
                break;
        }
        return message;
    }
}
