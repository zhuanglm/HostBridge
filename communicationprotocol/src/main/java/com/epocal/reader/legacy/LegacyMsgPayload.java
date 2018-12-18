package com.epocal.reader.legacy;

import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.MessageDescriptor;
import com.epocal.util.UnsignedType;

import java.util.Arrays;

/**
 * Created by rzhuang on July 30 2018.
 */

public abstract class LegacyMsgPayload implements IMsgPayload {
    public abstract MessageDescriptor getDescriptor();

    public byte getMessageCode() {
        return mMessageCode;
    }

    public void setMessageCode(byte messagecode) {
        mMessageCode = messagecode;
    }

    public byte[] getRawBuffer() {
        return mRawbuffer;
    }

    public void setRawBuffer(byte[] rawbuffer) {
        if (mRawbuffer == null) {
            mRawbuffer = new byte[rawbuffer.length];
        }
        System.arraycopy(rawbuffer, 0, mRawbuffer, 0, Math.min(rawbuffer.length, mRawbuffer.length));
    }

    public abstract int fillBuffer();

    public abstract ParseResult parseBuffer(byte[] buffer);

    private byte mMessageCode;

    private byte[] mRawbuffer;

    public abstract String toString();

//    public float makeFloat(byte[] buffer, int at)
//    {
//        byte[] temp = new byte[4];
//
//        temp[0] = (byte) (buffer[at + 3] & 0xff);
//        temp[1] = (byte) (buffer[at + 2] & 0xff);
//        temp[2] = (byte) (buffer[at + 1] & 0xff);
//        temp[3] = (byte) (buffer[at] & 0xff);
//
//        return BigEndianBitConverter.toSingle(temp, 0);
//    }

//    public short getShort(byte[] bytes, int at) {
////        int v =  (0xff & bytes[at] |
////                (0xff00 & bytes[at + 1] << 8)) ;
////        return (short)(v & 0x0FFFF);
//        short v1 = (short)((0xff & bytes[at] <<8)|
//                (0xff00 & bytes[at + 1]));
//        return (short)(v1 & 0x0FFFF);
//    }

    private int getRevInt(byte[] buffer, int at) {
        int val = (0xff000000 & (buffer[at] << 24)) |
                (0x00ff0000 & (buffer[at + 1] << 16)) |
                (0x0000ff00 & (buffer[at + 2] << 8)) |
                (0x000000ff & buffer[at + 3]);
        return val & 0x0FFFFFFFF;
    }

    public static int getInt(byte[] bytes, int at) {
        return (0xff & bytes[at]) |
                (0xff00 & (bytes[at + 1] << 8)) |
                (0xff0000 & (bytes[at + 2] << 16)) |
                (0xff000000 & (bytes[at + 3] << 24));
    }

    protected float getFloat(byte[] buffer, int at) {
        return Float.intBitsToFloat(getInt(buffer, at));
    }

    protected float getRevFloat(byte[] buffer, int at) {
        return Float.intBitsToFloat(getRevInt(buffer, at));
    }

//    public static long getLong(byte[] buffer, int index) {
//        return 	(0xff00000000000000L & ((long)buffer[index+0] << 56))  |
//                (0x00ff000000000000L & ((long)buffer[index+1] << 48))  |
//                (0x0000ff0000000000L & ((long)buffer[index+2] << 40))  |
//                (0x000000ff00000000L & ((long)buffer[index+3] << 32))  |
//                (0x00000000ff000000L & ((long)buffer[index+4] << 24))  |
//                (0x0000000000ff0000L & ((long)buffer[index+5] << 16))  |
//                (0x000000000000ff00L & ((long)buffer[index+6] << 8))   |
//                (0x00000000000000ffL &  (long)buffer[index+7]);
//    }

//    public static double getDouble(byte[] buffer, int index) {
//        return Double.longBitsToDouble(getLong(buffer, index));
//    }

    protected byte[] int24tobytes(long val) {
        byte[] bt24 = new byte[3];
        byte[] bt64 = toByteArray(val);

        bt24[0] = bt64[5];
        bt24[1] = bt64[6];
        bt24[2] = bt64[7];

        return bt24;
    }


    public byte[] toByteArray(long val) {
        byte[] bt = new byte[8];
        bt[0] = (byte) (val >> 56);
        bt[1] = (byte) (val >> 48);
        bt[2] = (byte) (val >> 40);
        bt[3] = (byte) (val >> 32);
        bt[4] = (byte) (val >> 24);
        bt[5] = (byte) (val >> 16);
        bt[6] = (byte) (val >> 8);
        bt[7] = (byte) val;

        return bt;
    }

    protected byte[] toByteArray(int val) {
        byte[] bt = new byte[4];

        bt[0] = (byte) ((val & 0xff000000) >> 24);
        bt[1] = (byte) ((val & 0x00ff0000) >> 16);
        bt[2] = (byte) ((val & 0x0000ff00) >> 8);
        bt[3] = (byte) (val & 0x000000ff);

        return bt;
    }

    protected byte[] toByteArray(short val) {
        byte[] bt = new byte[2];

        bt[0] = (byte) ((val & 0xff00) >> 8);
        bt[1] = (byte) (val & 0x00ff);

        return bt;
    }

    protected byte[] toByteArray(float val) {
        int intVal = Float.floatToIntBits(val);
        return toByteArray(intVal);
    }

    protected byte[] toByteArray(double val) {
        long intVal = Double.doubleToLongBits(val);
        return toByteArray(intVal);
    }

    protected static short createint16(byte byte1, byte byte2) {
        short int1 = UnsignedType.UInt16.ConvertToUInt16((short) byte1);
        short int2 = UnsignedType.UInt16.ConvertToUInt16((short) byte2);

        int1 <<= 8;

        return (short) (int1 | int2);
    }

    protected static int createint24(byte byte1, byte byte2, byte byte3) {
        int tempInt;

        tempInt = byte1;
        tempInt <<= 8;
        tempInt |= byte2;
        tempInt <<= 8;
        tempInt |= byte3;

        return tempInt;
    }

    protected byte[] fillBufferWithType(String type, String value) {
        byte[] buffer;
        try {
            if (type.equals("byte")) {
                buffer = new byte[1];
                buffer[0] = Byte.parseByte(value);
                return buffer;
            } else if (type.equals("uint16") || type.equals("int16")) {
                return toByteArray(Short.parseShort(value));
            } else if (type.equals("uint32") || type.equals("int32")) {
                return toByteArray(Integer.getInteger(value));
            } else if (type.equals("float")) {
                return toByteArray(Float.parseFloat(value));
            } else if (type.equals("double")) {
                return toByteArray(Double.parseDouble(value));
            } else if (type.equals("string")) {
                // fill in the ascii encoding
                return value.getBytes();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public class SequenceVersion {
        public byte major;
        public byte minor;
        public byte revision;
    }

    protected void appendCRC() {
        byte crc = (byte) 0xaa;

        for (byte b : mRawbuffer)
            crc ^= b;

        byte[] buffer = new byte[mRawbuffer.length + 1];
        System.arraycopy(mRawbuffer, 0, buffer, 0, mRawbuffer.length);
        buffer[buffer.length - 1] = crc;
        mRawbuffer = Arrays.copyOf(buffer, buffer.length);
    }

    private static byte charToByteAscii(char ch) {
        return (byte) ch;
    }

    protected static String stringToAscii(String value) {
        StringBuilder sbu = new StringBuilder();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append(charToByteAscii(chars[i]));
            }
        }
        return sbu.toString();
    }

}
