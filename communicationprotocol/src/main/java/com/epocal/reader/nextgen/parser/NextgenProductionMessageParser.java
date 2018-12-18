package com.epocal.reader.nextgen.parser;

import com.epocal.reader.IMsgPayload;
import com.epocal.reader.MsgPayload;
import com.epocal.reader.PreliminaryMessage;

/**
 * Created by dning on 6/12/2017.
 */

public class NextgenProductionMessageParser {
    public NextgenProductionMessageParser(){}

    public MsgPayload parse(PreliminaryMessage msg)
    {
        MsgPayload message = null;
        /**
        switch (msg.descriptor().MsgGroup)
        {
            case ReaderProtocolLibrary.NextGen.Enums.MessageGroup.Configuration:
            {
                switch (Mode)
                {
                    case ParsingMode.Request:
                        message = CreateProductionConfigurationRequest((ReaderProtocolLibrary.NextGen.Enums.ProductionConfigurationRequest)msg.descriptor().Type);
                        break;
                    case ParsingMode.Response:
                        message = CreateProductionConfigurationResponse((ReaderProtocolLibrary.NextGen.Enums.ProductionConfigurationResponse)msg.descriptor().Type);
                        break;
                }
            }
            break;
        }
        **/
        return message;
    }
}
