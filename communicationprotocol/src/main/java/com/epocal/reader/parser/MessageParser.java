package com.epocal.reader.parser;

import android.util.Log;

import com.epocal.reader.IMessageParserCallback;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.NextGenPacketHeader;
import com.epocal.reader.PreliminaryMessage;
import com.epocal.reader.ProtocolChecker;
import com.epocal.reader.enumtype.DestinationDevice;
import com.epocal.reader.enumtype.ParsingMode;
import com.epocal.reader.enumtype.ProtoGeneration;
import com.epocal.reader.enumtype.SourceDevice;
import com.epocal.reader.legacy.parser.LegacyMessageParser;
import com.epocal.reader.nextgen.message.CorruptedMessage;
import com.epocal.reader.nextgen.parser.NextgenMessageParser;
import com.epocal.reader.type.MessageDescriptor;
import com.epocal.util.ByteUtil;
import com.epocal.util.RefWrappers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by dning on 6/8/2017.
 */

public class MessageParser implements IMessageParser {
    public static final int MAX_SENDBUFFER = 5000;

    private ByteArrayOutputStream mRawStream;
    private boolean mIsParsing;
    private NextgenMessageParser mNextgenMessageParser;
    private LegacyMessageParser mLegacyMessageParser;
//    private Object mInputObject;    //added by rzhuang for TestDataProcessor

    private ParsingMode mParsingMode;

    public ParsingMode getMode() {
        return mParsingMode;
    }

    public void setMode(ParsingMode mMode) {
        this.mParsingMode = mMode;
    }

    private IMessageParserCallback mIMessageParserCallback;

    public void setIMessageParserCallback(IMessageParserCallback mIMessageParserCallback) {
        this.mIMessageParserCallback = mIMessageParserCallback;
    }

    public MessageParser(ParsingMode parsingMode) {
        mParsingMode = parsingMode;
        mIsParsing = false;
        mRawStream = new ByteArrayOutputStream();
        mNextgenMessageParser = new NextgenMessageParser();
        mLegacyMessageParser = new LegacyMessageParser();
    }


    final private char[] hexArray = "0123456789ABCDEF".toCharArray();

    @SuppressWarnings("all")
    private String bytesToHex(@Nonnull byte[] bytes, int startidx, int length) {
        char[] hexChars = new char[length * 3];
        for (int j = startidx; j < length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    @Override
    public void addData(byte[] buffer, int startidx, int length) {
        if (buffer.length < startidx + length)
            return;
        if (mRawStream == null) {
            mRawStream = new ByteArrayOutputStream();
        }
        mRawStream.write(buffer, startidx, length);

        byte[] data = mRawStream.toByteArray();
        if (data.length >= 4 && (data[3] & 0xff) != 0xca) {
            Log.d("Raymond Test", "head segment error");
        }
        //Log.d("Raymond Test", "Added data: "+bytesToHex(data,0,data.length));

    }

    @Override
    public IMsgPayload parse(Object userparam) {
        if (mIsParsing) {
            return null;
        }
        mIsParsing = true;
        List<PreliminaryMessage> nextgenMsgs = new ArrayList<>();
        byte[] data = mRawStream.toByteArray();

        List<Integer> preambleIndexes = indexOfSequence(data, ProtocolChecker.Preamble, 0);
        if (preambleIndexes.size() <= 0) {
            mIsParsing = false;
            if (data.length > MAX_SENDBUFFER)        //some cases received to many unavailable data
                mRawStream.reset();
            return null; //no premables found. does not contain a partial or full message
        }
        mRawStream.reset();

        for (Integer idx : preambleIndexes) {
            int fullMsgLen = ProtocolChecker.fullMessageLength(data, idx, data.length - idx);
            if (fullMsgLen == -1) {
                //write unparsed bytes back into the memorystream
                mRawStream.write(data, idx, data.length - idx);
                mIsParsing = false;
                break;
            }

            //SANITY CHECKS
            //1. check whether byte array has full message
            if (data.length < idx + fullMsgLen) {
                //write unparsed bytes back into the memorystream
                mRawStream.write(data, idx, data.length - idx);
                mIsParsing = false;
                //Log.d("Raymond test","saved data to next time :"+bytesToHex(data,0,data.length));
                break;
            }

            //2. check whether byte array includes ends of packet
            if (data[idx + fullMsgLen - 1] != ProtocolChecker.EndOfPacket) {
                if (data.length > idx + fullMsgLen) {
                    Log.d("Raymond test", "package length does not match real length error");
                } else {
                    Log.d("Raymond test", "missed data package: " + bytesToHex(data, 0, fullMsgLen));
                }
                continue; //partial msg, could be corrupt
            }

            byte[] fullMessage = new byte[fullMsgLen];
            System.arraycopy(data, idx, fullMessage, 0, fullMsgLen);
//            Log.d("Raymond test","package type : "+fullMessage[4]);
//            Log.d("Raymond Test", "Full message : "+bytesToHex(fullMessage,0,fullMsgLen));

            //3. check that there is not more than 1 premable in this message
            if (indexOfSequence(fullMessage, ProtocolChecker.Preamble, 0).size() > 1)
                continue; //contains multiple preambles. corrupt and overlapping another message

            //fire message event
            //if (RawMessagePayload != null)
            //    RawMessagePayload(this, new RawMessageEventArgs(fullMessage) { userParam = userparam });

            ProtoGeneration proto = ProtocolChecker.getProtocolGeneration(fullMessage, 0, fullMessage.length);
            if (proto != ProtoGeneration.Legacy) {
                try {
                    PreliminaryMessage nextGenMessage = new PreliminaryMessage(fullMessage);
                    nextgenMsgs.add(nextGenMessage);
                } catch (IOException e) {
                    break;
                }
            } else { //added by rzhuang at July 25 2018 for Legacy
                try {
//                    if (userparam != null)
//                        mInputObject = userparam;   // save TestDataProcessor
                    PreliminaryMessage legacyMessage = new PreliminaryMessage(fullMessage, proto);
                    nextgenMsgs.add(legacyMessage);
                } catch (IOException e) {
                    break;
                }
            }
        }

        if (mIsParsing && data[data.length - 1] != ProtocolChecker.EndOfPacket) {
            int idx = data.length - 1;
            for (; idx > 0; idx--) {
                if (data[idx] == ProtocolChecker.EndOfPacket)
                    break;
            }

            if (idx > 0) {
                mRawStream.write(data, idx + 1, data.length - idx - 1);
                mIsParsing = false;
            }
        }

        IMsgPayload message = null;
        for (int i = 0; i < nextgenMsgs.size(); i++) {
            PreliminaryMessage msg = nextgenMsgs.get(i);
            try {
                msg.setParsingMode(mParsingMode);
                message = parseMessage(msg);
            } catch (Exception ex) {
                message = new CorruptedMessage();
                message.parseBuffer(msg.getArray());
            }
            if (message == null) {
                message = new CorruptedMessage();
                message.parseBuffer(msg.getArray());
            }

            //fire event for message
            if (mIMessageParserCallback != null)
                mIMessageParserCallback.parsedMessageReceived(message);
        }

        //fire nothing to parse event
        //if(rawStream.Length == 0 && DiagnosticMode)
        //{
        //    if (NothingToParse != null)
        //        NothingToParse(this, new EventArgs());
        //}
        mIsParsing = false;
        return message;
    }

    @SuppressWarnings("all")
    private List<Integer> indexOfSequence(byte[] buffer, byte[] pattern, int startIndex) {
        int sIndex = startIndex;
        List<Integer> positions = new ArrayList<>();

        int i = ByteUtil.indexOf(buffer, sIndex, buffer.length, pattern[0]);
        while (i >= 0 && i <= buffer.length - pattern.length) {
            byte[] segment = Arrays.copyOfRange(buffer, i, i + pattern.length);
            if (Arrays.equals(segment, pattern)) {
                //check if the byte follow segment is 0xDE, if it is, may be head is
                // C0 DE C0 DE C0
                if (buffer.length > i + pattern.length && buffer[i + pattern.length] == pattern[1]) {
                    Log.d("Raymond test", "package head segment error");
                    i += 2;
                    continue;
                }

                positions.add(i);
                sIndex = i + pattern.length;
                i = ByteUtil.indexOf(buffer, sIndex, buffer.length, pattern[0]);
            } else {
                sIndex = i + 1;
                i = ByteUtil.indexOf(buffer, sIndex, buffer.length, pattern[0]);
            }
        }
        return positions;
    }

    private IMsgPayload parseMessage(PreliminaryMessage msg) {
        IMsgPayload messagePayload = null;
        MessageDescriptor descriptor = msg.descriptor();
        switch (descriptor.getMsgInterface()) {
            case NextGen: {
                messagePayload = mNextgenMessageParser.parse(msg);
                break;
            }
            case Legacy: {
                messagePayload = mLegacyMessageParser.parse(msg/*, mInputObject*/);
                break;
            }
        }
        if (messagePayload == null)
            messagePayload = new CorruptedMessage();

        messagePayload.parseBuffer(msg.getPayload());
        return messagePayload;
    }

    // will fill up a buffer and then send it onto the connection object
    public int parseMessage(IMsgPayload message, int messageNumber, RefWrappers<byte[]> Wrapperbuffer) {
        PacketFactory pf = new PacketFactory();
        NextGenPacketHeader packHeader = new NextGenPacketHeader();
        packHeader.setSourceDevice(SourceDevice.EpocHost.value);
        packHeader.setDestinationDevice(DestinationDevice.EpocReader.value);
        packHeader.setPacketNumber((short) messageNumber);   //added for Legacy package usage
        Wrapperbuffer.setRef(pf.createPayload(packHeader, message));
        return Wrapperbuffer.getRef().length;
    }
}
