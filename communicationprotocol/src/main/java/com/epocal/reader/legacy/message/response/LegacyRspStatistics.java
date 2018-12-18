package com.epocal.reader.legacy.message.response;

import android.util.Log;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.legacy.message.request.command.LegacyReqStatistics;
import com.epocal.reader.protocolcommontype.HostErrorCode;
import com.epocal.reader.type.MessageDescriptor;

import java.text.NumberFormat;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import static com.epocal.reader.common.EpocTime.timePOSIXMilliseconds2009;

/**
 * Created by rzhuang on July 31 2018.
 */

public class LegacyRspStatistics extends LegacyMsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.StatisticsResponse.value);
    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    private LegacyReqStatistics.StatisticsType mStatisticsType;
    private int mNumStatistics;
    public ArrayList<IndividualStatistics> statisticsList;
    public LegacyRspStatistics() {
        statisticsList = new ArrayList();
    }

    public static class IndividualStatistics implements Comparable<IndividualStatistics> {
        private short mTestCount;
        private long mHostDateTime;
        private int mHostId;
        private short mReaderReturnCode;
        private short mTestReturnCode;
        private short mTestReturnCodeCategory;
        private int mSpare;
        public static int sizeOfStatistics;

        static {
            sizeOfStatistics = 18;
        }

        static HostErrorCode[] errorCodesMaintenance = { HostErrorCode.CalNotDetected,
                HostErrorCode.RealtimeQCFailedDuringFluidics,
                HostErrorCode.RealtimeQCFailedDuringSampleIntro,
                HostErrorCode.RealtimeQCFailedDuringSampling,
                HostErrorCode.FluidicsFailedQCDuringCalibration,
                HostErrorCode.FluidicsFailedQCDuringSampleIntro,
                HostErrorCode.FluidicsFailedQCDuringSampling,
                HostErrorCode.ThermalQCFailedDuringFluidics,
                HostErrorCode.ThermalQCFailedDuringSampleIntro,
                HostErrorCode.ThermalQCFailedDuringSampling,
                HostErrorCode.HematocritLowResistance,
                HostErrorCode.ReferenceBubble };

        IndividualStatistics(byte[] buffer, int at)
        {
            try {
                mTestCount = BigEndianBitConverter.toInt16(buffer, 0);
                mHostDateTime = createint24(buffer[at + 2], buffer[at + 3], buffer[at + 4]);
                mHostId = createint24(buffer[at + 5], buffer[at + 6], buffer[at + 7]);
                mReaderReturnCode = createint16(buffer[at + 8], buffer[at + 9]);
                mTestReturnCode = createint16(buffer[at + 10], buffer[at + 11]);
                mTestReturnCodeCategory = createint16(buffer[at + 12], buffer[at + 13]);
                mSpare = BigEndianBitConverter.toInt32(buffer, 14);
            } catch (Exception e) {
                Log.e(LegacyMessageType.StatisticsResponse.name(),e.getLocalizedMessage());
            }
        }

        public IndividualStatistics(short testId, short hostErrorCode, short hostErrorCategory)
        {
            mTestCount = testId;
            mTestReturnCode = hostErrorCode;
            mTestReturnCodeCategory = hostErrorCategory;

            // number of minutes since sep 1st 2009
            mHostDateTime = timePOSIXMilliseconds2009();
        }



//    protected UnsignedType.UInt32 createint32(byte byte1, byte byte2, byte byte3, byte byte4)
//    {
//        UnsignedType.UInt32 tempInt = 0;
//
//        tempInt = byte1;
//        tempInt <<= 8;
//        tempInt |= byte2;
//        tempInt <<= 8;
//        tempInt |= byte3;
//        tempInt <<= 8;
//        tempInt |= byte4;
//
//        return tempInt;
//    }

        @Override
        public int compareTo(@Nonnull IndividualStatistics is) {
            if (mHostDateTime > is.mHostDateTime)
            {
                return -1;
            }
            else if (mHostDateTime < is.mHostDateTime)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
    }

    public static boolean determineReaderMaintenanceFailure(int OutOfHowMany,
                      ArrayList<IndividualStatistics> individualStatisticsList, int limit)
    {
        int counter;

        for (int j = 0; j < IndividualStatistics.errorCodesMaintenance.length; j++)
        {
            counter = 0;

            for (int i = 0; i < Math.min(OutOfHowMany, individualStatisticsList.size()); i++)
            {
                if (individualStatisticsList.get(i).mTestReturnCode ==
                        (short)(IndividualStatistics.errorCodesMaintenance[j].value))
                {
                    counter++;
                }
            }

            if (counter >= limit)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        // handle however many bytes are incoming.. code is first
        mStatisticsType = LegacyReqStatistics.StatisticsType.convert(buffer[0]);

        // get number of statistics that are coming in
        mNumStatistics = createint16(buffer[2], buffer[1]);

        if (mStatisticsType == LegacyReqStatistics.StatisticsType.maintenance)
        {
            for (int i = 0; i < (mNumStatistics / IndividualStatistics.sizeOfStatistics); i++)
            {
                statisticsList.add(new IndividualStatistics(buffer, 3 + i * IndividualStatistics.sizeOfStatistics));
            }
        }

        return ParseResult.Success;
    }

    @Override
    public String toString() {
        NumberFormat numFormat = NumberFormat.getNumberInstance();
        StringBuilder tempString = new StringBuilder("Stats: " + numFormat.format(mNumStatistics)
                + " " + numFormat.format(mStatisticsType.value));

        if (mStatisticsType == LegacyReqStatistics.StatisticsType.maintenance)
        {
            for (int i = 0; i < statisticsList.size(); i++)
            {
                tempString.append(" #").append(numFormat.format(i)).append(" ")
                        .append(statisticsList.get(i).toString());
            }
        }

        return tempString.toString();

    }
}
