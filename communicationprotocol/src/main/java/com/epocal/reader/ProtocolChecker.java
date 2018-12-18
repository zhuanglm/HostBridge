package com.epocal.reader;

import android.util.Log;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.enumtype.ProtoGeneration;

import java.util.Arrays;

import static com.epocal.reader.common.BigEndianBitConverter.byte2short;

/**
 * Created by dning on 6/9/2017.
 */

public final class ProtocolChecker {
    static final int LegacyHeaderLength = 10;
    static final int NextgenHeaderLength = 16;

    static final int LegacyMsgLengthOffset = 7;
    static final int NextgenMsgLengthOffset = 12;

    static final int MsgLengthBytesNum = 2;

    static final int MessageDescriptor = 4;
    static final int PayloadCRC = 2;

    static final int LegacyPacketEOP = 0; //
    static final int NextGenPacketEOP = 1; //end of packet

    static final int NextGenMinimumSize = NextgenHeaderLength + MessageDescriptor + PayloadCRC + NextGenPacketEOP;
    static final int LegacyMinimumSize = LegacyHeaderLength + LegacyPacketEOP;

    public static byte [] Preamble = new byte[] { (byte)0xC0, (byte)0xDE, (byte)0xC0 };
    public static byte EndOfPacket = (byte)0xAA;

    public static boolean isValidMessage(byte[] packet, int startidx, int length)
    {
        if (packet == null || packet.length - startidx < Math.min(LegacyMinimumSize, NextGenMinimumSize))
            return false;
        if (!Arrays.equals(Arrays.copyOfRange(packet, startidx, startidx + 3),Preamble))
            return false;
        return true;
    }

    public static int fullMessageLength(byte[] packet, int startidx, int length)
    {
        if (!isValidMessage(packet, startidx, length))
            return -1;

        ProtoGeneration generation = getProtocolGeneration(packet, startidx, length);
        if (generation == ProtoGeneration.Unknown)
        {
            return -1;
        }

        int headerlengthWithEOP = generation == ProtoGeneration.NextGen ? (NextgenHeaderLength + NextGenPacketEOP) : (LegacyHeaderLength + LegacyPacketEOP);
        short msglen = 0;
        try
        {
            // modified by rzhuang for add Legacy process
            int nMsgLenOffset = generation != ProtoGeneration.Legacy ? NextgenMsgLengthOffset : LegacyMsgLengthOffset;
//            if (generation != ProtoGeneration.Legacy)
//            {
//                byte[] dataLength = Arrays.copyOfRange(packet, startidx + 12, startidx + 12 + 2);
//                msglen = (short) BigEndianBitConverter.toInt16(dataLength, 0);
//            }

            byte[] dataLength = Arrays.copyOfRange(packet, startidx + nMsgLenOffset,
                    startidx + nMsgLenOffset + MsgLengthBytesNum);
            //msglen = BigEndianBitConverter.toInt16(dataLength, 0);
            msglen = byte2short(dataLength);
            if (msglen < 0) {
                Log.d("Raymond test", "fullMsgLen: " + msglen);
                Log.d("Raymond test", "fullMsgLen from : " + dataLength.toString());
                msglen = 0;
            }
        }
        catch (Exception ex)
        {
            return -1;
        }

        return headerlengthWithEOP + msglen; //full messagea length
    }

    public static ProtoGeneration getProtocolGeneration(byte[] packet, int startidx, int length)
    {
        int generationLocation = startidx + 3;
        if (packet != null && packet.length >= Math.min(LegacyMinimumSize, NextGenMinimumSize))
        {
            //**************added by rzhuang
            //JAVA ,0XCA -> int = -54 , can not match in covert()
            int value = getUnsignedByte(packet[generationLocation]);
            //*****************
            if (generationLocation < packet.length - 1) {
//                if (ProtoGeneration.convert((int) packet[generationLocation]) != null)
//                    return ProtoGeneration.convert((int) packet[generationLocation]);
                if (ProtoGeneration.convert(value) != null)
                    return ProtoGeneration.convert(value);
            }
        }
        return ProtoGeneration.Unknown;
    }

    //**********added by rzhuang
    public static int getUnsignedByte (byte data){
        return data&0x0FF;
    }
}
