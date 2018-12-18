package com.epocal.epoctest.type;

import com.epocal.epoctest.enumtype.BarcodeVerificationCode;
import com.epocal.reader.common.EpocTime;
import com.epocal.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dning on 7/26/2017.
 */

public class BarcodeInformation {

    private int mCardType;
    private Calendar mCardMade;
    private String mBarcodeString = "";

    public int getCardType() {
        return mCardType;
    }

    public Calendar getCardMade() {
//        Calendar cal = Calendar.getInstance();
//        return cal;
        if (mCardMade == null)
            mCardMade = Calendar.getInstance();
        return mCardMade;
    }

    public void setCardType(int mCardType) {
        this.mCardType = mCardType;
    }

    public String getBarcodeString() {
        return mBarcodeString;
    }

    public void setBarcodeString(String mBarcodeString) {
        this.mBarcodeString = mBarcodeString;
    }

    public void reset() {
        mBarcodeString = "";

        mCardType = mExpiryMonths = mLotNumberYear =
                mLotNumberDay = mDayLot = mDaysFromDayZero = 0;
        mCardSerialNumber = -1;
        mCardMade = mCardExpiry = Calendar.getInstance();
        mStyle = BarcodeStyle.None;
    }

    public BarcodeVerificationCode decode(byte[] insertMsgBarcode, int barcodeLength) {
        parseBarcodeString(insertMsgBarcode, barcodeLength);

        mCardMade = Calendar.getInstance();
        mCardExpiry = mCardMade;

        return BarcodeVerificationCode.Success;
    }

    private boolean parseBarcodeString(byte[] insertMsgBarcode, int barcodeLength) {
        if (barcodeLength == 0)
            return false;

        StringBuilder sTemp = new StringBuilder();
        for (int i = 0; i < barcodeLength; i++) {
            if (insertMsgBarcode[i] == 0xFF) {
                return false;
            }
            sTemp.append(String.valueOf((insertMsgBarcode[i] / 10)));
            sTemp.append(String.valueOf((insertMsgBarcode[i] % 10)));
        }
        mBarcodeString = sTemp.toString();
        return true;
    }

    //added by rzhuang for Legacy
    public BarcodeInformation() {
        reset();
    }

    public enum BarcodeStyle {None, Length4, Length6, Length10, Length14Old, Length14New, TwoDimensional}

    public enum CardUsageFlags {
        HumanCard(0), VetCard(1), SpecialVetCard(2);
        public final int value;

        CardUsageFlags(int value) {
            this.value = value;
        }
    }

    private BarcodeStyle mStyle;
    public Calendar mCardExpiry;
    private String mInternalBarcodeString;
    private int mDayLot;
    private int mLotNumberYear;
    private int mLotNumberDay;
    private Calendar mDateOfSwitchToNewSuffixes = Calendar.getInstance();
    public int mLineNumber = 0;
    private int mCardUsageFlag;     //HumanCard = 0, VetCard = 1, SpecialVetCard = 2

    private int mDaysFromDayZero;
    private int mExpiryMonths;
    private int mCardSerialNumber;
    private static boolean daysIncludeOnlyWeekDays6 = false;
    private static int[] ExpiryDaysTypeCard4BarCode6 = {140, 168, 200, 115};
    private static int NumDaysPerMonthOfExpiry = 28;
    private static int[][] ExpiryDaysPerCard6DigitBarcode = {{140, 168},  // 0
            {140, 168},  // 1
            {140, 168},  // 2
            {140, 168},  // 3
            {0, 0},  // 4
            {140, 168},  // 5
            {140, 168},  // 6
            {200, 168},  // 7
            {200, 168},  // 8
            {140, 168},  // 9
            {140, 168},  // 10
            {140, 168},  // 11
            {140, 112},  // 12
            {112, 84},  // 13
            {63, 84},  // 14
            {112, 42}}; // 15

    private static Date[] DayZero = {
            DateUtil.CreateDate("2014-03-03", "yyyy-MM-dd").getTime(),  // card type 0
            DateUtil.CreateDate("2012-10-15", "yyyy-MM-dd").getTime(),  // card type 1
            DateUtil.CreateDate("2015-08-03", "yyyy-MM-dd").getTime(),  // card type 2
            DateUtil.CreateDate("2010-05-31", "yyyy-MM-dd").getTime(),  // card type 3
            DateUtil.CreateDate("2010-05-31", "yyyy-MM-dd").getTime(),  // card type 4
            DateUtil.CreateDate("2012-10-14", "yyyy-MM-dd").getTime(),  // card type 5
            DateUtil.CreateDate("2012-06-04", "yyyy-MM-dd").getTime(),  // card type 6 crea/cl- day zero is monday june 6th
            DateUtil.CreateDate("2010-05-31", "yyyy-MM-dd").getTime(),  // card type 7
            DateUtil.CreateDate("2015-08-03", "yyyy-MM-dd").getTime(),  // card type 8
            DateUtil.CreateDate("2015-08-03", "yyyy-MM-dd").getTime(),  // card type 9
            DateUtil.CreateDate("2015-08-03", "yyyy-MM-dd").getTime(),  // card type 10
            DateUtil.CreateDate("2015-08-03", "yyyy-MM-dd").getTime(),  // card type 11
            DateUtil.CreateDate("2015-08-03", "yyyy-MM-dd").getTime(),  // card type 12
            DateUtil.CreateDate("2015-08-03", "yyyy-MM-dd").getTime(),  // card type 13
            DateUtil.CreateDate("2014-03-03", "yyyy-MM-dd").getTime(),  // card type 14
            DateUtil.CreateDate("2014-03-03", "yyyy-MM-dd").getTime(),};    // card type 15

    private BarcodeVerificationCode decodeBarcodeGeneric6Length(String barcodeString,
                                                                int numBitsDays,
                                                                int numBitsExpiry1,
                                                                int numBitsProductCode,
                                                                int numBitsExpiry2,
                                                                int numBitsLotNumber,
                                                                int numBitsAdditionalCardTypes,
                                                                int numBitsCardSerialNumber,
                                                                boolean includeOnlyWeekDays,
                                                                boolean sublotExtendsDate) {
        int totalBits = numBitsDays + numBitsExpiry1 + numBitsProductCode + numBitsExpiry2 + numBitsLotNumber + numBitsAdditionalCardTypes + numBitsCardSerialNumber;
        long barcodeInt = Long.parseLong(barcodeString);
        long limit = 1;
        long tempbarcodeInt;
        long tempbarcodeInt16;
        long expiryInfo;
        int expiryDays = 0;

        limit = (limit << totalBits) - 1;

        if (barcodeInt > limit) {
            return BarcodeVerificationCode.Invalid;
        }

        mDaysFromDayZero = (int) (barcodeInt & (((1 << numBitsDays) - 1)));
        tempbarcodeInt = (barcodeInt = (barcodeInt >> numBitsDays)) & (((1 << numBitsExpiry1) - 1));
        mCardType = (int) ((barcodeInt = (barcodeInt >> numBitsExpiry1)) & (((1 << numBitsProductCode) - 1)));
        tempbarcodeInt16 = ((barcodeInt = (barcodeInt >> numBitsProductCode)) & (((1 << numBitsExpiry2) - 1)));
        if (mCardType == 4) {
            expiryInfo = ((tempbarcodeInt16 << numBitsExpiry1) | tempbarcodeInt);
        } else {
            expiryInfo = tempbarcodeInt;
        }
        mDayLot = (int) ((barcodeInt = (barcodeInt >> numBitsExpiry2)) & ((((1 << numBitsLotNumber) - 1))));
        if (mCardType != 4) {
            if (tempbarcodeInt16 == 1) {
                mDayLot += 4;
            }
        }

        if (mDayLot == 1 || mDayLot == 4) {
            // vet line 1 and 2
            mCardUsageFlag = CardUsageFlags.VetCard.value;
        } else if (mDayLot == 2 || mDayLot == 5) {
            // heska line 1 and 2
            mCardUsageFlag = CardUsageFlags.SpecialVetCard.value;
        } else {
            // human lines 1-4
            mCardUsageFlag = CardUsageFlags.HumanCard.value;
        }

        // set the line number based on daylot. leave it as 0 if the daylot doesn't match anything
        if ((mDayLot == 0) || (mDayLot == 1) || (mDayLot == 2)) {
            mLineNumber = 1;
        } else if ((mDayLot == 3) || (mDayLot == 4) || (mDayLot == 5)) {
            mLineNumber = 2;
        } else if (mDayLot == 6) {
            mLineNumber = 3;
        } else if (mDayLot == 7) {
            mLineNumber = 4;
        }

        if (sublotExtendsDate) {
            // daylot is actually an extension of card made date.. we will use it as such and then set the cardtype
            // number back to 0
            mDaysFromDayZero += (int) (mDayLot * Math.pow(2, numBitsDays));

            // set the day lot back to 0. 1 will not be used in this format
            mDayLot = 0;
        }

        // this used to be 2 digits in the old barcode.. so we'll turn it back into 2 digits
        mLotNumberYear = mCardMade.get(Calendar.YEAR) - 2000;
        mLotNumberDay = mCardMade.get(Calendar.DAY_OF_YEAR);

        int additionalCardTypes = (int) ((barcodeInt = (barcodeInt >> numBitsLotNumber)) & (((1 << numBitsAdditionalCardTypes) - 1)));

        // new for 3.7. what used to be expiry months now means additional card types
        if (additionalCardTypes != 0) {
            mCardType += 4;
        }

        if (mCardType == 4) {
            if (expiryInfo >= 0 && expiryInfo < 4) {
                expiryDays = ExpiryDaysTypeCard4BarCode6[(int) expiryInfo];
                mExpiryMonths = (int) Math.round((double) expiryDays / (double) NumDaysPerMonthOfExpiry);
            }
        } else {
            if (expiryInfo >= 0 && expiryInfo < 2) {
                expiryDays = ExpiryDaysPerCard6DigitBarcode[mCardType][(int) expiryInfo];
                mExpiryMonths = (int) Math.round((double) expiryDays / (double) NumDaysPerMonthOfExpiry);
            }
        }
        Date dayZero;

        if (mCardType > DayZero.length)
            dayZero = DayZero[DayZero.length - 1];
        else
            dayZero = DayZero[mCardType];

        Calendar cardmade = Calendar.getInstance();
        cardmade.setTime(dayZero);
        if (includeOnlyWeekDays) {
            int numberOfWeeks = mDaysFromDayZero / 5;
            cardmade.add(Calendar.DAY_OF_YEAR, numberOfWeeks * 7 + (mDaysFromDayZero % 5));
        } else {
            cardmade.add(Calendar.DAY_OF_YEAR, mDaysFromDayZero);
        }
        mCardMade = cardmade;

        // add the number of days per month for the number of months
        mCardExpiry = (Calendar) mCardMade.clone();
        mCardExpiry.add(Calendar.DAY_OF_YEAR, expiryDays);
        if (mCardType == 0) mCardType = 111;

        if (numBitsCardSerialNumber != 0) {
            mCardSerialNumber = (int) ((barcodeInt >> numBitsAdditionalCardTypes)
                    & (((1 << numBitsCardSerialNumber) - 1)));
        } else {
            mCardSerialNumber = -1;
        }

        // the card can't be made in the future
        if (mCardMade.getTime().getTime() > EpocTime.now().getTime()) {
            return BarcodeVerificationCode.ExpiryInTheFuture;
        }

        // check the expiry against todays date
        if (mCardExpiry.getTime().getTime() + (24 * 60 * 60 * 1000) < EpocTime.now().getTime()) {
            return BarcodeVerificationCode.Expired;
        }

        return BarcodeVerificationCode.Success;
    }

    private BarcodeVerificationCode decodeBarcode6Length(String barcodeString) {
        return decodeBarcodeGeneric6Length(barcodeString, 11, 1, 4,
                1, 2, 0, 0, BarcodeInformation.daysIncludeOnlyWeekDays6, false);
    }

    public BarcodeVerificationCode decodeLegacy(byte[] insertMsgBarcode, int barcodeLength) {
        if (!parseBarcodeString(insertMsgBarcode, barcodeLength)) {
            return BarcodeVerificationCode.Failure;
        }

        // 4 length barcodes valid for humans
//        if (mBarcodeString.length() == 4)
//        {
//            //return DecodeBarcode4Length(mBarcodeString);
//        }
        if (mBarcodeString.length() == 6) {
            return decodeBarcode6Length(mBarcodeString);
        } else {
            return BarcodeVerificationCode.Invalid;
        }

    }

    public String getLotNumberStringWithDash() {
        Calendar cal = Calendar.getInstance();
        cal.set(2009, Calendar.SEPTEMBER, 16);
        mDateOfSwitchToNewSuffixes.set(2015, 12, 9);

        if ((mCardType == 0) && (mCardMade.compareTo(cal) < 0) && (mStyle == BarcodeStyle.Length4)) {
            // old style SHORT barcode
            return "0" + mInternalBarcodeString + "-" + mDayLot;
        } else {
            // if the date of creation of the card is greater than or equal midnight december 9th 2015
            if (mCardMade.compareTo(mDateOfSwitchToNewSuffixes) >= 0) {
                return String.format(Locale.getDefault(), "%02d-%s%03d-%d%d", mCardType, new SimpleDateFormat("yy", Locale.getDefault()).format(mCardMade.getTime()),
                        mCardMade.get(Calendar.DAY_OF_YEAR), mLineNumber, mCardUsageFlag);

            } else {
                return String.format(Locale.getDefault(), "%02d-%s%03d-%d%d", mCardType, new SimpleDateFormat("yy", Locale.getDefault()).format(mCardMade.getTime()),
                        mCardMade.get(Calendar.DAY_OF_YEAR), mDayLot, (mCardUsageFlag == CardUsageFlags.VetCard.value
                                || mCardUsageFlag == CardUsageFlags.SpecialVetCard.value) ? 1 : 0);

            }
        }
    }
}
