package com.epocal.common.types;

import com.epocal.util.EnumSetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public enum BarcodeSymbologiesType {

        UPC_A (0x00000001)
                {@Override
                public String toString() {
            return "UPC A";
        }},
        UPC_E (0x00000002)
                {@Override
                public String toString() {
            return "UPC E";
        }},
        UPC_E1 (0x00000004)
                {@Override
                public String toString() {
            return "UPC E1";
        }},
        EAN_8 (0x00000008)
                {@Override
                public String toString() {
                    return "UEAN 8";
                }},
        EAN_13 (0x00000010)
                {@Override
                public String toString() {
                    return "UEAN 13";
                }},
        Bookland_EAN (0x00000020)
                {@Override
                public String toString() {
                    return "Bookland EAN";
                }},
        Code_128 (0x00000040)
                {@Override
                public String toString() {
                    return "Code 128";
                }},
        UCC_EAN_128 (0x00000080)
                {@Override
                public String toString() {
                    return "UCC EAN 128";
                }},
        ISBT_1281 (0x00000100)
                {@Override
                public String toString() {
                    return "ISBT 1281";
                }},
        Code_39 (0x00000200)
                {@Override
                public String toString() {
                    return "Code 39";
                }},
        Trioptic_Code_39 (0x00000400)
                {@Override
                public String toString() {
                    return "Trioptic Code 39";
                }},
        Code_39_Full_ASCII_Conversion (0x00000800)
                {@Override
                public String toString() {
                    return "Code 39 Full ASCII Conversion";
                }},
        Code_93 (0x00001000)
                {@Override
                public String toString() {
                    return "Code 93";
                }},
        Interleaved_2_of_5 (0x00002000)
                {@Override
                public String toString() {
                    return "Interleaved 2 of 5";
                }},
        Discrete_2_of_5 (0x00004000)
                {@Override
                public String toString() {
                    return "Discrete 2 of 5";
                }},
        Codabar (0x00008000)
                {@Override
                public String toString() {
                    return "Codabar";
                }},
        MSI (0x00010000)
                {@Override
                public String toString() {
                    return "MSI";
                }},

        // 2d barcodes
        PDF417 (0x00020000)
                {@Override
                public String toString() {
                    return "PDF 417";
                }},
        MicroPDF (0x00040000)
                {@Override
                public String toString() {
                    return "Micro PDF";
                }},
        MacroPDF (0x00080000)
                {@Override
                public String toString() {
                    return "Macro PDF";
                }},
        MaxiCode (0x00100000)
                {@Override
                public String toString() {
                    return "Maxi Code";
                }},
        DataMatrix (0x00200000)
                {@Override
                public String toString() {
                    return "Data Matrix";
                }},
        QRCode (0x00400000)
                {@Override
                public String toString() {
                    return "QRCode";
                }},
        MacroMicroPDF (0x00800000)
                {@Override
                public String toString() {
                    return "MacroMicro PDF";
                }},
        CompositeAB (0x01000000)
                {@Override
                public String toString() {
                    return "Composite AB";
                }},
        CompositeC (0x02000000)
                {@Override
                public String toString() {
                    return "Composite C";
                }},
        TLC39 (0x04000000)
                {@Override
                public String toString() {
                    return "TLC 39";
                }},
        Aztec (0x08000000)
                {@Override
                public String toString() {
                    return "Aztec";
                }},
        MicroQR (0x10000000)
                {@Override
                public String toString() {
                    return "MicroQR";
                }},

        noBarcode (0x00000000),
        All2dMasks (0x1FFE0000),
        AllMasks (0x1FFFFFFF);

    public final long value;
    BarcodeSymbologiesType(long value)
    {
        this.value = Long.valueOf(value);
    }
    private static final Map<Long,BarcodeSymbologiesType> typeMap = new HashMap<Long,BarcodeSymbologiesType>();
    static {
        for (BarcodeSymbologiesType type : BarcodeSymbologiesType.values()){
            typeMap.put(type.value,type);
        }
    }

    public static BarcodeSymbologiesType fromInt(long i){
        BarcodeSymbologiesType retval = typeMap.get(Long.valueOf(i));
        if (retval ==null){
            return BarcodeSymbologiesType.noBarcode;
        }
        return retval;

    }

    public static ArrayList<BarcodeSymbologiesType> getSupportedItemList(long value){
        ArrayList<BarcodeSymbologiesType> list = new ArrayList<BarcodeSymbologiesType>();
        for (BarcodeSymbologiesType type : BarcodeSymbologiesType.values()){
            if ((type.value & value) != 0)
            {
                list.add(type);
            }
        }
        return list;
    }

    public static ArrayList<BarcodeSymbologiesType> get1DBardcodeList(){
        ArrayList<BarcodeSymbologiesType> list = new ArrayList<BarcodeSymbologiesType>();
        for (BarcodeSymbologiesType type : BarcodeSymbologiesType.values()){
            if (type.value >= BarcodeSymbologiesType.UPC_A.value && type.value <= BarcodeSymbologiesType.MSI.value)
            {
                list.add(type);
            }
        }
        return list;
    }

    public static ArrayList<BarcodeSymbologiesType> get2DBardcodeList(){
        ArrayList<BarcodeSymbologiesType> list = new ArrayList<BarcodeSymbologiesType>();
        for (BarcodeSymbologiesType type : BarcodeSymbologiesType.values()){
            if (type.value >= BarcodeSymbologiesType.PDF417.value && type.value <= BarcodeSymbologiesType.MicroQR.value)
            {
                list.add(type);
            }
        }
        return list;
    }

    public static boolean isEnabledSymbologies(long value, BarcodeSymbologiesType barcodeSymbologiesType){

        if ((barcodeSymbologiesType.value & value) != 0)
        {
            return true;
        }
        return false;
    }
}
