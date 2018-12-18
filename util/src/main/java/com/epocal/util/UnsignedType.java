package com.epocal.util;

/**
 * Created by dning on 7/7/2017.
 */

public class UnsignedType
{
    static public class UInt16 {
        private short mValue = 0;
        public UInt16(){}
        public UInt16(short mValue)
        {
            this.mValue = mValue;
        }
        public short getValue() {
            return (short)(mValue & 0xFF);
        }

        public void setValue(short mValue) {
            this.mValue = mValue;
        }

        public static short ConvertToUInt16(short signedInt16)
        {
            return (short)(signedInt16 & 0xFF);
        }
    }

    static public class UInt32 {
        private int mValue = 0;
        public UInt32(){}
        public UInt32(int mValue)
        {
            this.mValue = mValue;
        }
        public int getValue() {
            return mValue & 0xFFFF;
        }

        public void setValue(int mValue) {
            this.mValue = mValue;
        }
    }
}
