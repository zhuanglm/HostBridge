package com.epocal.reader.nextgen.parser;

import com.epocal.reader.IMsgPayload;
import com.epocal.reader.PreliminaryMessage;
import com.epocal.reader.enumtype.GenericAckRequest;
import com.epocal.reader.enumtype.GenericActionResponse;
import com.epocal.reader.enumtype.GenericCommandRequest;
import com.epocal.reader.enumtype.GenericCommandResponse;
import com.epocal.reader.enumtype.GenericConfigurationRequest;
import com.epocal.reader.enumtype.GenericConfigurationResponse;
import com.epocal.reader.enumtype.GenericDebugResponse;
import com.epocal.reader.enumtype.GenericErrorResponse;
import com.epocal.reader.enumtype.GenericInterfaceRequest;
import com.epocal.reader.enumtype.GenericInterfaceResponse;
import com.epocal.reader.enumtype.GenericNotificationResponse;
import com.epocal.reader.enumtype.GenericSessionRequest;
import com.epocal.reader.enumtype.GenericSessionResponse;

/**
 * Created by dning on 6/12/2017.
 */

public class NextgenGenericMessageParser
{
    public NextgenGenericMessageParser(){}

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
                        message = createGenericCommandRequest((GenericCommandRequest.convert(msg.descriptor().getType())));
                        break;
                    case Response:
                        message = createGenericCommandResponse((GenericCommandResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
            case Action:
            {
                switch (msg.getParsingMode())
                {
                    case Request:
                        //message = createGenericActionRequest((GenericActionRequest.convert(msg.descriptor().getType())));
                        break;
                    case Response:
                        message = createGenericActionResponse((GenericActionResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
            case Ack:
            {
                switch (msg.getParsingMode())
                {
                    case Request:
                        message = createGenericAckRequest(GenericAckRequest.convert(msg.descriptor().getType()));
                        break;
                    case Response:
                        //message = CreateGenericAckResponse((ReaderProtocolLibrary.NextGen.Enums.GenericAckResponse)msg.descriptor().Type);
                        break;
                }
            }
            break;
            case Notification:
            {
                switch (msg.getParsingMode())
                {
                    case Response:
                        message = createGenericNotificationResponse((GenericNotificationResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
            case Configuration:
            {
                switch (msg.getParsingMode())
                {
                    case Request:
                        message = createGenericConfigurationRequest((GenericConfigurationRequest.convert(msg.descriptor().getType())));
                        break;
                    case Response:
                        message = createGenericConfigurationResponse((GenericConfigurationResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
            case Interface:
            {
                switch (msg.getParsingMode())
                {
                    case Request:
                        message = createGenericInterfaceRequest((GenericInterfaceRequest.convert(msg.descriptor().getType())));
                        break;
                    case Response:
                        message = createGenericInterfaceResponse((GenericInterfaceResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
            case Session:
            {
                switch (msg.getParsingMode())
                {
                    case Request:
                        message = createGenericSessionRequest((GenericSessionRequest.convert(msg.descriptor().getType())));
                        break;
                    case Response:
                        message = createGenericSessionResponse((GenericSessionResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
            case Error:
            {
                switch (msg.getParsingMode())
                {
                    case Response:
                        message = createGenericErrorResponse((GenericErrorResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
            case Debug:
            {
                switch (msg.getParsingMode())
                {
                    case Response:
                        message = CreateGenericDebugResponse((GenericDebugResponse.convert(msg.descriptor().getType())));
                        break;
                }
            }
            break;
        }
        return message;
    }

    IMsgPayload createGenericCommandRequest(GenericCommandRequest msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case CardRequest:
                message = new com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqCard();
                break;
            case DeviceEnable:
                //message = new GenCmdReqDeviceEnable();
                break;
            case DeviceIdRequest:
                message = new com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqDeviceId();
                break;
            case DeviceStatusRequest:
                message = new com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqDeviceStatus();
                break;
            case DeviceAuxInfoRequest:
                //message = new GenCmdReqDeviceAuxiliaryInfo();
                break;
            case DevicePage:
                //message = new GenCmdReqDevicePage();
                break;
            case HostIdInfo:
                message = new com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqHostId();
                break;
            case Results:
                //message = new GenCmdReqResults();
                break;
            case StatisticsRequest:
                //message = new GenCmdReqStatistics();
                break;
            case PerformDryCard:
                message = new com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqPerformDryCardCheck();
                break;
            case PerformEqc:
                message = new com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqPerformEQC();
                break;
            case PerformTest:
                message = new com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqPerformTest();
                break;
        }
        return message;
    }

    IMsgPayload createGenericCommandResponse(GenericCommandResponse msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case CardResponse:
                message = new com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspCard();
                break;
            case DeviceEnable:
                //message = new ReaderProtocolLibrary.NextGen.Messages.Response.Generic.Command.GenCmdRspDeviceEnable();
                break;
            case DeviceIdResponse:
                message = new com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspDeviceId();
                break;
            case DeviceStatusResponse:
                message = new com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspDeviceStatus();
                break;
            case DeviceAuxInfoResponse:
                //message = new ReaderProtocolLibrary.NextGen.Messages.Response.Generic.Command.GenCmdRspDeviceAuxiliaryInfo();
                break;
            case DevicePage:
                //message = new ReaderProtocolLibrary.NextGen.Messages.Response.Generic.Command.GenCmdRspDevicePage();
                break;
            case HostIdInfo:
                message = new com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspHostId();
                break;
            case Results:
                //message = new ReaderProtocolLibrary.NextGen.Messages.Response.Generic.Command.GenCmdRspResults();
                break;
            case StatisticsResponse:
                //message = new ReaderProtocolLibrary.NextGen.Messages.Response.Generic.Command.GenCmdRspStatistics();
                break;
            case PerformDryCard:
                message = new com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspPerformDryCardCheck();
                break;
            case PerformEqc:
                message = new com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspPerformEQC();
                break;
            case PerformTest:
                message = new com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspPerformTest();
                break;
        }
        return message;
    }

    IMsgPayload createGenericActionResponse(GenericActionResponse msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case eQC:
                message = new com.epocal.reader.nextgen.message.response.generic.action.GenActRspEQC();
                break;
            case DryCardCheck:
                message = new com.epocal.reader.nextgen.message.response.generic.action.GenActRspDryCardCheck();
                break;
        }
        return message;
    }

    IMsgPayload createGenericAckRequest(GenericAckRequest msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case NotDefined:
                message = new com.epocal.reader.nextgen.message.request.generic.ack.GenAckReqAck();
                break;
        }
        return message;
    }

    IMsgPayload createGenericInterfaceRequest(GenericInterfaceRequest msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case Bge:
                message = new com.epocal.reader.nextgen.message.request.generic.interfaces.GenIntReqBgeInterface();
                break;
            case Barcode:
                message = new com.epocal.reader.nextgen.message.request.generic.interfaces.GenIntReqBarcodeInterface();
                break;
        }
        return message;
    }

    IMsgPayload createGenericInterfaceResponse(GenericInterfaceResponse msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case Bge:
                message = new com.epocal.reader.nextgen.message.response.generic.interfaces.GenIntRspBgeInterface();
                break;
            case Barcode:
                message = new com.epocal.reader.nextgen.message.response.generic.interfaces.GenIntRspBarcodeInterface();
                break;
        }
        return message;
    }

    IMsgPayload createGenericConfigurationRequest(GenericConfigurationRequest msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case General:
                message = new com.epocal.reader.nextgen.message.request.generic.configuration.GenCfgReqGeneral();
                break;
            case SIBeQC:
                message = new com.epocal.reader.nextgen.message.request.generic.configuration.GenCfgReqSIBeQC();
                break;
            case DryCardCheck:
                message = new com.epocal.reader.nextgen.message.request.generic.configuration.GenCfgReqDryCardCheck();
                break;
        }
        return message;
    }

    IMsgPayload createGenericConfigurationResponse(GenericConfigurationResponse msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case General:
                message = new com.epocal.reader.nextgen.message.response.generic.configuration.GenCfgRspGeneral();
                break;
            case SIBeQC:
                message = new com.epocal.reader.nextgen.message.response.generic.configuration.GenCfgRspSIBeQC();
                break;
            case DryCardCheck:
                message = new com.epocal.reader.nextgen.message.response.generic.configuration.GenCfgRspDryCardCheck();
                break;
        }
        return message;
    }

    IMsgPayload createGenericNotificationResponse(GenericNotificationResponse msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case CommandStarted:
                message = new com.epocal.reader.nextgen.message.response.generic.notification.GenNtfRspCommandStarted();
                break;
            case CommandFinished:
                message = new  com.epocal.reader.nextgen.message.response.generic.notification.GenNtfRspCommandFinished();
                break;
            case Card:
                message = new  com.epocal.reader.nextgen.message.response.generic.notification.GenNtfRspCard();
                break;
        }
        return message;
    }

    IMsgPayload createGenericSessionRequest(GenericSessionRequest msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case ReaderSettings:
                message = new com.epocal.reader.nextgen.message.request.generic.session.GenSsnReqSessionReaderSettings();
                break;
            case Eqc:
                message = new com.epocal.reader.nextgen.message.request.generic.session.GenSsnReqSessionEqc();
                break;
            case PatientTest:
                message = new com.epocal.reader.nextgen.message.request.generic.session.GenSsnReqSessionPatientTest();
                break;
        }
        return message;
    }

    IMsgPayload createGenericSessionResponse(GenericSessionResponse msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case ReaderSettings:
                message = new com.epocal.reader.nextgen.message.response.generic.session.GenSsnRspSessionReaderSettings();
                break;
            case Eqc:
                message = new  com.epocal.reader.nextgen.message.response.generic.session.GenSsnRspSessionEQC();
                break;
            case PatientTest:
                message = new  com.epocal.reader.nextgen.message.response.generic.session.GenSsnRspSessionPatientTest();
                break;
        }
        return message;
    }

    IMsgPayload CreateGenericDebugResponse(GenericDebugResponse msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case DebugDevice:
                message = new com.epocal.reader.nextgen.message.response.generic.debug.GenDbgRspDebugDevice();
                break;
            case Information:
                message = new com.epocal.reader.nextgen.message.response.generic.debug.GenDbgRspDebugDevice();
                break;
        }
        return message;
    }

    IMsgPayload createGenericErrorResponse(GenericErrorResponse msgType)
    {
        IMsgPayload message = null;
        switch (msgType)
        {
            case CommunicationInterface:
                message = new com.epocal.reader.nextgen.message.response.generic.error.GenErrRspCommunicationInterface();
                break;
            case Firmware:
                message = new com.epocal.reader.nextgen.message.response.generic.error.GenErrRspFirmware();
                break;
            case Device:
                message = new com.epocal.reader.nextgen.message.response.generic.error.GenErrRspDevice();
                break;
        }
        return message;
    }
}
