package com.epocal.reader.legacy.message.response;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.DAMStages;
import com.epocal.reader.enumtype.legacy.DataPacketType;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.enumtype.legacy.ReaderMode;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

import java.text.NumberFormat;

/**
 * Created by rzhuang on Aug 3 2018.
 */

public class LegacyRspDataPacket extends LegacyMsgPayload {

    public interface IDataPacketGetConfig {
        int getBlockSize(DataPacketType packetType, int blockNumber);
    }

    public static void setInterface(IDataPacketGetConfig config) {
        mDataPacketInterface = config;
    }

    private static IDataPacketGetConfig mDataPacketInterface;

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.DataPacket.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    private ReaderMode readerMode;
    private DAMStages DAMMode;
    private DataPacketType packetType;
    private byte blockNumber;
    private byte mSequenceLength;
    private int NUM_BYTES_QC_CHECK = 3;
    private float[] mSamples;
    private byte[] mQcResults;
    private byte mConductivityByte;
    private byte mDebuggingInformation;
    private float mFloat1;
    private float mFloat2;
    private float mFloat3;
    private float mFloat4;
    private byte mMscCounter;
    private byte mMscInfo;
    private byte mMscCRC;
    private byte mArmCRC;
    private float mPid1;
    private float mPid2;

    public LegacyRspDataPacket(/*Object param*/) {
        //mTr = (Integer) param;
    }

    public ReaderMode getReaderMode() {
        return readerMode;
    }

    public DAMStages getDAMMode() {
        return DAMMode;
    }

    public DataPacketType getPacketType() {
        return packetType;
    }

    public byte getBlockNumber() {
        return blockNumber;
    }

    public float[] getSamples() {
        return mSamples;
    }

    public byte getConductivityByte() {
        return mConductivityByte;
    }

    public float getPid2() {
        return mPid2;
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        int i, j;

        int count = buffer.length;

        if (count < 4)
            return ParseResult.BufferLengthIncorrect;

        readerMode = ReaderMode.convert(buffer[0]);

        DAMMode = DAMStages.convert(buffer[1]);
        packetType = DataPacketType.convert(buffer[2]);
        blockNumber = buffer[3];

        mSequenceLength = (byte) mDataPacketInterface.getBlockSize(packetType, blockNumber);

        if (count < 4 + NUM_BYTES_QC_CHECK + 4 * mSequenceLength +
                (packetType == DataPacketType.TestData ? 30 : 0))
            return ParseResult.BufferLengthIncorrect;

        mSamples = new float[mSequenceLength];

        // its all qc results from now on
        mQcResults = new byte[NUM_BYTES_QC_CHECK];

        // qc results is however many bits it takes to store 1 bit for each sequence number
        for (i = 0; i < NUM_BYTES_QC_CHECK; i++) {
            mQcResults[i] = buffer[i + 4];
        }

        if (packetType == DataPacketType.TestData) {
            // conductivity byte after qc results
            mConductivityByte = buffer[NUM_BYTES_QC_CHECK + 4];

            for (j = 0; j < mSequenceLength; j++) {
                mSamples[j] = getRevFloat(buffer, 5 + NUM_BYTES_QC_CHECK + j * 4);
            }

            mDebuggingInformation = buffer[5 + NUM_BYTES_QC_CHECK + mSequenceLength * 4];
            mFloat1 = getRevFloat(buffer, 6 + NUM_BYTES_QC_CHECK + mSequenceLength * 4);
            mFloat2 = getRevFloat(buffer, 10 + NUM_BYTES_QC_CHECK + mSequenceLength * 4);
            mFloat3 = getRevFloat(buffer, 14 + NUM_BYTES_QC_CHECK + mSequenceLength * 4);
            mFloat4 = getRevFloat(buffer, 18 + NUM_BYTES_QC_CHECK + mSequenceLength * 4);
            mPid1 = getRevFloat(buffer, 22 + NUM_BYTES_QC_CHECK + mSequenceLength * 4);
            mPid2 = getRevFloat(buffer, 26 + NUM_BYTES_QC_CHECK + mSequenceLength * 4);
            mMscCounter = buffer[30 + NUM_BYTES_QC_CHECK + mSequenceLength * 4];
            mMscCRC = buffer[31 + NUM_BYTES_QC_CHECK + mSequenceLength * 4];
            mMscInfo = buffer[32 + NUM_BYTES_QC_CHECK + mSequenceLength * 4];
            mArmCRC = buffer[33 + NUM_BYTES_QC_CHECK + mSequenceLength * 4];
        } else {
            for (j = 0; j < mSequenceLength; j++) {
                mSamples[j] = getRevFloat(buffer, 4 + NUM_BYTES_QC_CHECK + j * 4);
            }
        }

        return ParseResult.Success;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        NumberFormat numFormat = NumberFormat.getNumberInstance();

        switch (packetType) {
            case TestData:
                sb.setLength(200 + (mQcResults == null ? 3 : mQcResults.length * 3));

                sb.append("Packet . QC Results ");

                if (mQcResults != null)
                    for (int i = 0; i < mQcResults.length; i++) {
                        if (i > 0)
                            sb.append(' ');
                        sb.append(numFormat.format(mQcResults[i]));
                    }
                else
                    sb.append("N/A");

                sb.append(". Reader Mode:");
                sb.append(readerMode.toString());
                sb.append(". DAM Mode:");
                sb.append(DAMMode.toString());
                sb.append(". Cond Byte:");
                sb.append(numFormat.format(mConductivityByte));
                sb.append(". DAM Counter: ");
                sb.append(numFormat.format(mMscCounter));
                sb.append(". DAM CRC: ");
                sb.append(numFormat.format(mMscCRC));
                sb.append(". DAM Info: ");
                sb.append(numFormat.format(mMscInfo));
                sb.append(". ARM CRC: ");
                sb.append(numFormat.format(mArmCRC));
                break;

            case DryCardCheck:
                sb.setLength(200 + (mQcResults == null ? 3 : mQcResults.length * 3)
                        + (mSamples == null ? 3 : mSamples.length * 10));

                sb.append("DryCard");
                sb.append("Packet . Reader Mode:");
                sb.append(readerMode.toString());
                sb.append(". Block: ");
                sb.append(blockNumber);
                sb.append(". DAM Mode:");
                sb.append(DAMMode.toString());
                sb.append(". QC");
                sb.append(": ");

                if (mQcResults != null)
                    for (int i = 0; i < mQcResults.length; i++) {
                        if (i > 0)
                            sb.append(' ');
                        sb.append(numFormat.format(mQcResults[i]));
                    }
                else
                    sb.append("N/A");

                sb.append(". ");
                sb.append("Samples");
                sb.append(": ");

                if (mSamples != null)
                    for (int i = 0; i < mSamples.length; i++) {
                        if (i > 0)
                            sb.append(' ');
                        sb.append(numFormat.format(mSamples[i]));
                    }
                else
                    sb.append("N/A");
                break;

            case SelfCheck:
                sb.setLength(200 + (mQcResults == null ? 3 : mQcResults.length * 3) + (mSamples == null ? 3 : mSamples.length * (10 + 12)));
                sb.append("SelfCheck");
                sb.append("Packet . Reader Mode:");
                sb.append(readerMode.toString());
                sb.append(". DAM Mode:");
                sb.append(DAMMode.toString());
                sb.append(". Block: ");
                sb.append(blockNumber);
                sb.append(". SelfCheckResults");
                sb.append(": ");

                if (mQcResults != null)
                    for (int i = 0; i < mQcResults.length; i++) {
                        if (i > 0)
                            sb.append(' ');
                        sb.append(numFormat.format(mQcResults[i]));
                    }
                else
                    sb.append("N/A");

                sb.append(". Samples: Z ");

                if (mSamples != null)
                    for (int i = 0; i < mSamples.length; i++) {
                        if (i > 0)
                            sb.append(' ');
                        sb.append(numFormat.format(mSamples[i]));
                    }
                else
                    sb.append("N/A");

                sb.append("  Z  ");

                if (mSamples != null) {
                    byte[] floatBuffer;
                    for (float sample : mSamples) {
                        floatBuffer = toByteArray(sample);
                        for (byte fb : floatBuffer) {
                            sb.append(' ');
                            sb.append(numFormat.format(fb));
                        }
                    }
                } else
                    sb.append("N/A");
                break;

            default:
                break;
        }

        return sb.toString();
    }

    public String getSampleString() {
        NumberFormat numFormat = NumberFormat.getNumberInstance();
        StringBuilder tempString = new StringBuilder();
        tempString.setLength(mSamples.length * 10 + 200);

        if (mSamples != null) {
            for (float sample : mSamples) {
                tempString.append(numFormat.format(sample + "\t"));
            }
        }

        tempString.append(numFormat.format(mFloat1)).append("\t")
                .append(numFormat.format(mFloat2)).append("\t")
                .append(numFormat.format(mFloat3)).append("\t")
                .append(numFormat.format(mFloat4)).append("\t")
                .append(numFormat.format(mPid1)).append("\t")
                .append(numFormat.format(mPid2));

        return tempString.toString();
    }

}
