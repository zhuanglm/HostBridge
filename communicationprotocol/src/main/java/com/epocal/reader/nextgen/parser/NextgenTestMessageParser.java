package com.epocal.reader.nextgen.parser;

import com.epocal.reader.IMsgPayload;
import com.epocal.reader.MsgPayload;
import com.epocal.reader.PreliminaryMessage;
import com.epocal.reader.enumtype.TestAckRequest;
import com.epocal.reader.enumtype.TestActionResponse;
import com.epocal.reader.enumtype.TestNotificationResponse;

/**
 * Created by dning on 6/12/2017.
 */

public class NextgenTestMessageParser {
    public NextgenTestMessageParser(){}

    public IMsgPayload parse(PreliminaryMessage msg)
    {
        IMsgPayload message = null;
        switch (msg.descriptor().getMsgGroup())
        {
            case Command:
            {
                switch (msg.getParsingMode())
                {
                    case Request:
                        //message = createGenericCommandRequest((GenericCommandRequest.convert(msg.descriptor().getType())));
                        break;
                    case Response:
                        //message = createGenericCommandResponse((GenericCommandResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
            case Configuration:
            {
                switch (msg.getParsingMode())
                {
                    case Request:
                        //message = createGenericCommandRequest((GenericCommandRequest.convert(msg.descriptor().getType())));
                        break;
                    case Response:
                        //message = createGenericCommandResponse((GenericCommandResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
            case Action:
            {
                switch (msg.getParsingMode())
                {
                    case Request:
                        //message = createGenericCommandRequest((GenericCommandRequest.convert(msg.descriptor().getType())));
                        break;
                    case Response:
                        message = createTestActionResponse((TestActionResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
            case Ack:
            {
                switch (msg.getParsingMode())
                {
                    case Request:
                        message = createTestAckRequest((TestAckRequest.convert(msg.descriptor().getType())));
                        break;
                    case Response:
                        //message = createTestAckResponse((TestAckResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
            case Error:
            {
                switch (msg.getParsingMode())
                {
                    case Request:
                        //message = createGenericCommandRequest((GenericCommandRequest.convert(msg.descriptor().getType())));
                        break;
                    case Response:
                        //message = createGenericCommandResponse((GenericCommandResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
            case Notification:
            {
                switch (msg.getParsingMode())
                {
                    case Request:
                        //message = createGenericCommandRequest((GenericCommandRequest.convert(msg.descriptor().getType())));
                        break;
                    case Response:
                        message = createTestNotificationResponse((TestNotificationResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
        }
        return message;
    }

    IMsgPayload createTestNotificationResponse(TestNotificationResponse msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case TimingUpdate:
                message = new com.epocal.reader.nextgen.message.response.test.notification.TstNtfRspTimingUpdate();
                break;
            case SDMEvent:
                message = new  com.epocal.reader.nextgen.message.response.test.notification.TstNtfRspSDMEvent();
                break;
        }
        return message;
    }

    IMsgPayload createTestAckRequest(TestAckRequest msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case NotDefined:
                message = new com.epocal.reader.nextgen.message.request.test.ack.TstAckReqAck();
                break;
        }
        return message;
    }

    IMsgPayload createTestActionResponse(TestActionResponse msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case BGETest:
                message = new com.epocal.reader.nextgen.message.response.test.action.TstActRspBGETest();
                break;
            case DevicePeripheralMonitor:
                message = new com.epocal.reader.nextgen.message.response.test.action.TstActRspDevicePeripheralMonitor();
                break;
        }
        return message;
    }
}
