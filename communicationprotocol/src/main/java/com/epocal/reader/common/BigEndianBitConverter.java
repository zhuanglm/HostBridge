package com.epocal.reader.common;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by dning on 6/7/2017.
 */

/**
 * Converts base data types to an array of bytes, and an array of bytes to base data types.
 * All info taken from the meta data of System.BitConverter.
 * This implementation allows for Endianness consideration.
 */
public class BigEndianBitConverter {
    public static boolean IsLittleEndian = true;

    /**
     * Indicates the byte order ("endianess")
     *
     * @return
     */
    public static ByteOrder isLittleEndianSystem() {
        if (IsLittleEndian) {
            return ByteOrder.LITTLE_ENDIAN;
        } else {
            return ByteOrder.BIG_ENDIAN;
        }
    }

    /**
     * Returns the specified Boolean value as an array of bytes.
     * 0 is false; 0xFF is true. according to reader communication protocol
     * @param value a Boolean value.
     * @return An array of bytes with length 1.
     */
    public static byte[] getBytes(boolean value) {
        byte[] v = new byte[1];
        if (value)
        {
            v[0] = (byte)0xFF;
        }
        else
        {
            v[0] = (byte)0;
        }
        return v;
        /*
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteStream);
        try {
            stream.writeBoolean(value);
        } catch (IOException e) {
            return new byte[1];
        }
        return byteStream.toByteArray();
        */
    }

    /**
     * Returns the specified Unicode character value as an array of bytes.
     *
     * @param value A character to convert.
     * @return An array of bytes with length 2.
     */
    public static byte[] getBytes(char value) {
        ByteBuffer buffer = ByteBuffer.allocate(2).order(BigEndianBitConverter.isLittleEndianSystem());
        buffer.asCharBuffer().put(value);
        return buffer.array();
    }

    /**
     * Returns the specified 16-bit signed integer value as an array of bytes.
     *
     * @param value The number to convert.
     * @return An array of bytes with length 2.
     */
    public static byte[] getBytes(short value) {
        ByteBuffer buffer = ByteBuffer.allocate(2).order(BigEndianBitConverter.isLittleEndianSystem());
        buffer.asShortBuffer().put(value);
        return buffer.array();
    }

    /**
     * Returns the specified 32-bit signed integer value as an array of bytes.
     *
     * @param value The number to convert.
     * @return An array of bytes with length 4.
     */
    public static byte[] getBytes(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(BigEndianBitConverter.isLittleEndianSystem());
        buffer.asIntBuffer().put(value);
        return buffer.array();

        /*
        buffer[offset++] = (byte)value;
        buffer[offset++] = (byte)(value>>8);
        buffer[offset++] = (byte)(value>>16);
        buffer[offset++] = (byte)(value>>24);
        */
    }

    /**
     * Returns the specified double-precision floating point value as an array of bytes.
     *
     * @param value The number to convert.
     * @return An array of bytes with length 8.
     */
    public static byte[] getBytes(double value) {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(BigEndianBitConverter.isLittleEndianSystem());
        buffer.asDoubleBuffer().put(value);
        return buffer.array();
    }

    /**
     * Returns the specified single-precision floating point value as an array of bytes.
     *
     * @param value The number to convert.
     * @return An array of bytes with length 4.
     */
    public static byte[] getBytes(float value) {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(BigEndianBitConverter.isLittleEndianSystem());
        buffer.asFloatBuffer().put(value);
        return buffer.array();
    }

    /**
     * Returns the specified 64-bit signed integer value as an array of bytes.
     *
     * @param value The number to convert.
     * @return An array of bytes with length 8.
     */
    public static byte[] getBytes(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(BigEndianBitConverter.isLittleEndianSystem());
        buffer.asLongBuffer().put(value);
        return buffer.array();
    }

    /**
     * Returns a boolean converted from one bytes at a specified position in a byte array.
     *
     * @param dsr           custome data stream.
     * @return A boolean.
     */
    public static boolean toBoolean(DataStreamReader dsr) throws IOException {
        byte[] data = dsr.readBytes(1);
        if(data[0] == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns a boolean converted from one bytes at a specified position in a byte array.
     *
     * @param data           custome data stream.
     * @return A boolean.
     */
    public static boolean toBoolean(byte data) throws IOException {
        if (data == 0) {
            return false;
        } else {
            return true;
        }
    }
            /**
             * Returns a 16-bit signed integer converted from two bytes at a specified position in a byte array.
             *
             * @param dsr           custome data stream.
             * @param length        byte length for type.
             * @param startIndex    The starting position within value.
             * @return A 16-bit signed integer formed by two bytes beginning at startIndex.
             */
    public static short toInt16(DataStreamReader dsr, int length, int startIndex) throws IOException {
        byte[] data = dsr.readBytes(length);
        ByteBuffer buffer = ByteBuffer.wrap(data).order(BigEndianBitConverter.isLittleEndianSystem());
        return buffer.asShortBuffer().get(0);
    }


    public static short toInt16(byte[] data, int startIndex) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(data).order(BigEndianBitConverter.isLittleEndianSystem());
        return buffer.asShortBuffer().get(startIndex);
    }

    public static short byte2short(byte[] b) {
        short intValue = 0;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & 0xFF) << (8 * (1 - i));
        }
        return intValue;
    }

    /**
     * Returns a 16-bit unsigned integer converted from two bytes at a specified position in a byte array.
     *
     * @param dsr        data stream.
     * @param length     byte length for type.
     * @param startIndex The starting position within value.
     * @return A 16-bit unsigned integer formed by two bytes beginning at startIndex.
     */
    public static short toUInt16(DataStreamReader dsr, int length, int startIndex) throws IOException {
        byte[] data = dsr.readBytes(length);
        ByteBuffer buffer = ByteBuffer.wrap(data).order(BigEndianBitConverter.isLittleEndianSystem());
        return (short)(buffer.asShortBuffer().get(0) & 0xFF);
    }

    /**
     * Returns a 32-bit signed integer converted from four bytes at a specified position in a byte array.
     *
     * @param dsr        data stream.
     * @param length     byte length for type.
     * @param startIndex The starting position within value.
     * @return A 32-bit signed integer formed by four bytes beginning at startIndex.
     */
    public static int toInt32(DataStreamReader dsr, int length, int startIndex) throws IOException {
        byte[] data = dsr.readBytes(length);
        ByteBuffer buffer = ByteBuffer.wrap(data).order(BigEndianBitConverter.isLittleEndianSystem());
        return buffer.asIntBuffer().get(0);
    }

    public static int toInt32(byte[] data, int startIndex) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(data).order(BigEndianBitConverter.isLittleEndianSystem());
        return buffer.asIntBuffer().get(startIndex);
    }

    /**
     * Returns a 64-bit signed integer converted from eight bytes at a specified position in a byte array.
     *
     * @param value      An array of bytes.
     * @param startIndex The starting position within value.
     * @return A 64-bit signed integer formed by eight bytes beginning at startIndex.
     */
    public static long toInt64(byte[] value, int startIndex) {
        ByteBuffer buffer = ByteBuffer.wrap(value).order(BigEndianBitConverter.isLittleEndianSystem());
        return buffer.asLongBuffer().get(startIndex);
    }

    /**
     * Returns a single-precision floating point number converted from four bytes at a specified position in a byte array.
     *
     * @param value      An array of bytes.
     * @param startIndex The starting position within value.
     * @return A single-precision floating point number formed by four bytes beginning at startIndex.
     */
    public static float toSingle(byte[] value, int startIndex) {
        ByteBuffer buffer = ByteBuffer.wrap(value).order(BigEndianBitConverter.isLittleEndianSystem());
        return buffer.asFloatBuffer().get(startIndex);
    }

    /***
     * convert to float type from four bytes at a specified position in a byte array.
     * @param dsr
     * @param length
     * @param startIndex
     * @return float
     * @throws IOException
     */
    public static float toSingle(DataStreamReader dsr, int length, int startIndex) throws IOException {
        byte[] data = dsr.readBytes(length);
        ByteBuffer buffer = ByteBuffer.wrap(data).order(BigEndianBitConverter.isLittleEndianSystem());
        return buffer.asFloatBuffer().get(startIndex);
    }

    /**
     * Converts the numeric value of each element of a specified array of bytes
     * to its equivalent hexadecimal string representation.
     *
     * @param value An array of bytes.
     * @return A System.String of hexadecimal pairs separated by hyphens, where each pair
     * represents the corresponding element in value; for example, "7F-2C-4A".
     */
    public static String toString(byte[] value) {
        ByteBuffer buffer = ByteBuffer.wrap(value).order(BigEndianBitConverter.isLittleEndianSystem());
        return new String(buffer.array());
    }
}
