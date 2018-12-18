package com.epocal.reader.legacy.parser;

import com.epocal.reader.IMsgPayload;
import com.epocal.reader.PreliminaryMessage;
import com.epocal.reader.legacy.message.UnknownMessage;
import com.epocal.reader.legacy.message.response.ArmDebugInfo;
import com.epocal.reader.legacy.message.response.LegacyRspAck;
import com.epocal.reader.legacy.message.response.LegacyRspAlive;
import com.epocal.reader.legacy.message.response.LegacyRspCardDetected;
import com.epocal.reader.legacy.message.response.LegacyRspCardInserted;
import com.epocal.reader.legacy.message.response.LegacyRspCardRemoved;
import com.epocal.reader.legacy.message.response.LegacyRspConfiguration3;
import com.epocal.reader.legacy.message.response.LegacyRspCustomConfig;
import com.epocal.reader.legacy.message.response.LegacyRspDataPacket;
import com.epocal.reader.legacy.message.response.LegacyRspDeviceID;
import com.epocal.reader.legacy.message.response.LegacyRspError;
import com.epocal.reader.legacy.message.response.LegacyRspHCMCfg;
import com.epocal.reader.legacy.message.response.LegacyRspMessageReceived;
import com.epocal.reader.legacy.message.response.LegacyRspStatistics;
import com.epocal.reader.legacy.message.response.LegacyRspStatus;
import com.epocal.reader.legacy.message.response.LegacyRspStatusUpdate;
import com.epocal.reader.legacy.message.response.LegacyRspTestStopped;

import static com.epocal.reader.enumtype.legacy.LegacyMessageType.Ack;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.Alive;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.ArmDebugInfo;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.CardDetected;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.CardInserted;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.CardRemoved;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.ConfigurationResponse3;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.CustomConfigurationResponse;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.DataPacket;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.DeviceIdResponse;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.Error;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.HCMConfiguration;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.MessageReceived;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.ReaderStatusResponse;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.StatisticsResponse;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.StatusUpdate;
import static com.epocal.reader.enumtype.legacy.LegacyMessageType.TestStopped;

/**
 * Created by rzhuang on July/25/2018.
 */

public class LegacyMessageParser {

//    public interface IParserGetConfig
////    {
////        public int getBuffersize(int blocknumber);
////    }
////    public static void setParserInterface(IParserGetConfig testInterface)
////    {
////        mParserInterface = testInterface;
////    }
////    public static IParserGetConfig mParserInterface;

    public LegacyMessageParser() {

    }

    public IMsgPayload parse(PreliminaryMessage msg) {
        IMsgPayload message;
        byte bType = msg.descriptor().getType();

        if (bType == StatisticsResponse.value) {            //0x1C
            message = new LegacyRspStatistics();
        } else if (bType == DeviceIdResponse.value) {       //0x00
            message = new LegacyRspDeviceID();
        } else if (bType == ReaderStatusResponse.value) {   //0x01
            message = new LegacyRspStatus();
        } else if (bType == CardInserted.value) {           //0x02
            message = new LegacyRspCardInserted();
        } else if (bType == CardRemoved.value) {            //0x03
            message = new LegacyRspCardRemoved();
        } else if (bType == DataPacket.value) {             //0x06
            message = new LegacyRspDataPacket();
        } else if (bType == TestStopped.value) {            //0x07
            message = new LegacyRspTestStopped();
        } else if (bType == Ack.value) {                    //0x08
            message = new LegacyRspAck();
        } else if (bType == Error.value) {                  //0x09
            message = new LegacyRspError();
        } else if (bType == CustomConfigurationResponse.value) { //0x0F
            message = new LegacyRspCustomConfig();
        } else if (bType == ConfigurationResponse3.value) {  //0x10
            message = new LegacyRspConfiguration3();
        } else if (bType == Alive.value) {                  //0x11
            message = new LegacyRspAlive();
        } else if (bType == MessageReceived.value) {        //0x12
            message = new LegacyRspMessageReceived();
        } else if (bType == HCMConfiguration.value) {       //0x13
            message = new LegacyRspHCMCfg();
        } else if (bType == ArmDebugInfo.value) {           //0x14
            message = new ArmDebugInfo();
        } else if (bType == StatusUpdate.value) {           //0x16
            message = new LegacyRspStatusUpdate();
        } else if (bType == CardDetected.value) {           //0x17
            message = new LegacyRspCardDetected();
        } else {
            message = new UnknownMessage();
            ((UnknownMessage) message).supposedMessageType = bType;
        }


//        switch (LegacyMessageType.convert(bType))
//    {
//        default:
//            message = new UnknownMessage();
//            ((UnknownMessage)message).supposedMessageType = bType;
//            break;
//
//        case DeviceIdResponse:  //0x00
//            message = new LegacyRspDeviceID();
//            break;
//
//        case ReaderStatusResponse:   //0x01
//            message = new LegacyRspStatus();
//            break;
//
//        case CardInserted:      //0x02
//            message = new LegacyRspCardInserted();
//            break;
//
//        case DataPacket:      //0x06
//            message = new LegacyRspDataPacket(userParam);
//            break;
//
//        case TestStopped:       //0x07
//            message = new LegacyRspTestStopped();
//            break;
//
//        case Ack:      //0x08
//            message = new LegacyRspAck();
//            break;
//
//        case Error:      //0x09
//            message = new LegacyRspError();
//            break;
//
//        case Alive:       //0x11
//            message = new LegacyRspError();
//            break;

//        case MessageReceived:   //0x12
//            //message =
//            break;
//
//        case HCMConfiguration:   //0x13
//            //message = new LegacyRspHCMCfg();
//            break;

//        case ArmDebugInfo:      //0x14
//            message = new ArmDebugInfo();
//            break;
//
//        case StatisticsResponse:   //0x1C
//            message = new LegacyRspStatistics();
//            break;
//
//    }
        return message;
    }
}
